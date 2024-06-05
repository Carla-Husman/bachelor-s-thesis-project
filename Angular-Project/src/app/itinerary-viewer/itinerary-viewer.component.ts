import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatButtonModule} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {db} from "../db";
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {MatListModule} from "@angular/material/list";
import {OverlayModule} from "@angular/cdk/overlay";
import {GoogleMap, GoogleMapsModule} from "@angular/google-maps";
import {ItineraryService} from "../services/SendItinerary/itinerary.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-itinerary-viewer',
  standalone: true,
  imports: [
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIcon,
    NgOptimizedImage,
    MatListModule,
    MatIconModule,
    NgForOf,
    NgIf,
    OverlayModule,
    GoogleMapsModule
  ],
  templateUrl: './itinerary-viewer.component.html',
  styleUrl: './itinerary-viewer.component.css'
})

export class ItineraryViewerComponent implements OnInit {
  itineraries: any;
  stars: number[] = [];
  isOpenSchedule: boolean[] = [];
  isOpenPhone: boolean[] = [];
  isOpenWebsite: boolean[] = [];
  isOpenAddress: boolean[] = [];
  viewInfo: string[] = []
  type = 0;
  clicked = false;
  itinerariesPhoto = ''

  @ViewChild('myGoogleMap', {static: true}) map!: GoogleMap;
  mapOptions: google.maps.MapOptions = {};
  markers: ({
    options: { animation: google.maps.Animation };
    position: { lat: number; lng: number };
    label: { color: string; text: string };
    title: string;
  } | {
    options: { animation: null };
    position: { lat: number; lng: number };
    label: { color: string; text: string };
    title: string;
  })[] = [];

  state = 1; // 0 = from home, 1 = from suggest

  constructor(private _itinerary: ItineraryService, private _router: Router, private _route: ActivatedRoute) {
  }

  async ngOnInit() {
    try {
      this.stars = Array(5).fill(0).map((_, i) => i + 1);

      if (Number(this._route.snapshot.params['id']) == -1) {
        window.addEventListener("beforeunload", function (e) {
          const confirmationMessage = "\o/";
          e.returnValue = confirmationMessage;
          return confirmationMessage;
        });
        this.itineraries = this._itinerary.getResult();
        console.log(this.itineraries)
        this.itineraries.season = this.itineraries.season == null ? "Unknown" : this.itineraries.season.substring(0, 1).toUpperCase() + this.itineraries.season.substring(1).toLowerCase();
        this.itineraries.transport = this.itineraries.transport == null ? "Unknown" : this.itineraries.transport.substring(0, 1).toUpperCase() + this.itineraries.transport.substring(1).toLowerCase()
        this.itineraries.budget = this.itineraries.budget == null ? "Unknown" : this.itineraries.budget.substring(0, 1).toUpperCase() + this.itineraries.budget.substring(1).toLowerCase()
        this.itineraries.attendant = this.itineraries.attendant == null ? "Unknown" : this.itineraries.attendant.substring(0, 1).toUpperCase() + this.itineraries.attendant.substring(1).toLowerCase()
        console.log(this.itineraries)
        this.state = 1;
      } else {
        this.itineraries = await db.transaction('r', [db.itineraries], async () => {
          this.state = 0;
          return db.itineraries.get({id: Number(this._route.snapshot.params['id'])});
        }).catch(error => {
          console.error(error);
        });
      }
      console.log("dupa if")
      this.isOpenSchedule = this.itineraries.pois.map(() => false);
      this.isOpenPhone = this.itineraries.pois.map(() => false);
      this.isOpenSchedule = this.itineraries.pois.map(() => false);
      this.isOpenAddress = this.itineraries.pois.map(() => false);
      console.log("dupa ceva iniitializari")
      for (let poi of this.itineraries.pois) {
        console.log("in for")
        this.markers.push({
          position: {
            lat: poi.latitude,
            lng: poi.longitude
          },
          title: poi.name,
          label: {
            color: 'black',
            text: (this.markers.length + 1).toString(),
          },
          options: {
            animation: google.maps.Animation.DROP
          }
        });
      }
      console.log("dupa for")
      this.calculateMapCenterAndZoom();
      console.log("supa calculare centru si zoom")
      for (let i = 0; i < this.itineraries.pois.length; ++i) {
        this.viewInfo[i] = ""
      }
    } catch (error) {
      await this._router.navigate(["/home"])
    }
  }

  calculateMapCenterAndZoom() {
    if (this.markers.length === 0) {
      return;
    }

    // find min and max
    let minLat = this.markers[0].position.lat;
    let maxLat = this.markers[0].position.lat;
    let minLng = this.markers[0].position.lng;
    let maxLng = this.markers[0].position.lng;

    this.markers.forEach(marker => {
      minLat = Math.min(minLat, marker.position.lat);
      maxLat = Math.max(maxLat, marker.position.lat);
      minLng = Math.min(minLng, marker.position.lng);
      maxLng = Math.max(maxLng, marker.position.lng);
    });

    // average of lat and log
    const centerLat = (minLat + maxLat) / 2;
    const centerLng = (minLng + maxLng) / 2;

    // vertical and horizontal distance between bounds
    const latDelta = maxLat - minLat;
    const lngDelta = maxLng - minLng;

    // zoom based on vertical or horizontal distance, taking the max zoom
    const verticalZoom = Math.floor(Math.log2(180 / latDelta));
    const horizontalZoom = Math.floor(Math.log2(360 / lngDelta));
    const zoom = Math.max(verticalZoom, horizontalZoom);

    // set map options
    this.mapOptions = {
      center: {
        lat: centerLat,
        lng: centerLng
      },
      zoom: zoom,
    };
  }

  goToMyMarker(poi: any) {
    this.markers = this.markers.map(m => {
      if (m.position.lat === poi.latitude && m.position.lng === poi.longitude) {
        return {
          ...m,
          options: {
            animation: google.maps.Animation.BOUNCE
          }
        };
      } else {
        return {
          ...m,
          options: {
            animation: null
          }
        };
      }
    });
  }

  async saveItinerary() {
    let id_user = await db.transaction('r', [db.users], async () => {
      return db.users.get({username: window.localStorage.getItem('username')});
    }).catch(error => {
      console.error("Error occurred while fetching user:", error);
    });

    if (id_user == undefined) {
      return;
    }

    await db.itineraries.add({
      userId: id_user.id != undefined ? id_user.id : -1,
      photo: this.itineraries.photo,
      name: this.itineraries.name,
      destination: this.itineraries.destination,
      startingPoint: this.itineraries.startingPoint,
      season: this.itineraries.season,
      attendant: this.itineraries.attendant,
      transport: this.itineraries.transport,
      budget: this.itineraries.budget,
      distance: this.itineraries.distance,
      poisNumber: this.itineraries.poisNumber,
      highlights: this.itineraries.highlights,
      pois: this.itineraries.pois
    });
  }

  async deleteItinerary() {
    await db.itineraries.delete(Number(this._route.snapshot.params['id']));
    await this._router.navigate(['/home']);
  }

  clickToViewInformation(index: number, type: number) {
    switch (type) {
      case 1:
        this.viewInfo[index] = this.itineraries.pois[index].address
        break;
      case 2:
        this.viewInfo[index] = this.itineraries.pois[index].phone
        break;
      case 3:
        this.viewInfo[index] = this.itineraries.pois[index].website
        break;
      case 4:
        this.viewInfo[index] = this.itineraries.pois[index].schedule
        break;
      default:
        break;
    }

    this.type = type;

    for (let i = 0; i < this.itineraries.pois.length; i++) {
      if (i !== index) {
        this.viewInfo[i] = "";
      }
    }
  }
}
