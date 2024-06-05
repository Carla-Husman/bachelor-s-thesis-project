import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {MatButton, MatIconButton} from "@angular/material/button";
import {
  MatCard, MatCardActions,
  MatCardAvatar,
  MatCardContent,
  MatCardHeader, MatCardImage,
  MatCardSubtitle,
  MatCardTitle
} from "@angular/material/card";
import {db, Itineraries, Users} from "../db";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {MatActionList} from "@angular/material/list";
import {MatDivider} from "@angular/material/divider";
import {MatRipple} from "@angular/material/core";
import {
  MatAccordion, MatExpansionModule,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelTitle
} from "@angular/material/expansion";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatMenu,
    MatIcon,
    MatMenuTrigger,
    MatMenuItem,
    MatIconButton,
    MatCardAvatar,
    MatCardTitle,
    MatCardSubtitle,
    MatCardHeader,
    MatCardContent,
    MatCard,
    MatCardActions,
    FormsModule,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    SlickCarouselModule,
    NgForOf,
    MatActionList,
    MatButton,
    MatCardImage,
    NgOptimizedImage,
    MatDivider,
    MatRipple,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatAccordion,
    MatExpansionPanelDescription,
    MatExpansionModule,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  user: Users | undefined | void;
  numberOfItineraries = 0;
  option = 1;
  index = 0;
  vacationsList: any = [];
  displayed!: Itineraries | undefined;
  thisYear = new Date().getFullYear();
  itinerariesPhoto = ''
  @ViewChild('avatar') myAvatar!: ElementRef;

  constructor(private _router: Router) {
  }

  async ngOnInit() {
    if (window.localStorage.getItem('username') != undefined || window.localStorage.getItem('username') != null) {
      this.user = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: window.localStorage.getItem('username')});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });

      this.vacationsList = await db.transaction('r', [db.itineraries], async () => {
        return db.itineraries.toArray();
      }).catch(error => {
        console.error("Error occurred while fetching itineraries:", error);
      });

      if (this.vacationsList == undefined || this.user == undefined) {
        await this._router.navigate(["/account"]);
        return;
      }

      this.displayed = this.vacationsList.length != 0 ? this.vacationsList[0] : undefined;

      this.numberOfItineraries = this.vacationsList.length;

      const image = new Image();
      image.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        if (!ctx) {
          console.error("Failed to get canvas 2d context.");
          return;
        }

        canvas.width = image.width;
        canvas.height = image.height;
        ctx.drawImage(image, 0, 0);

        canvas.toBlob((blob) => {
          if (!blob) {
            console.error("Failed to create blob from canvas.");
            return;
          }

          const imageUrl = URL.createObjectURL(blob);

          const avatarElement = document.getElementById('avatar');
          if (avatarElement) {
            avatarElement.style.backgroundImage = `url('${imageUrl}')`;
          }
        });
      };
      image.src = this.user.src;
    } else {
      await this._router.navigate(["/account"]);
    }
  }

  async logout() {
    window.localStorage.removeItem('username');
    await this._router.navigate(['/account']);
  }

  changeOption(newOption: number) {
    this.option = newOption;
  }

  async back() {
    if (this.index != undefined && this.index != 0 && this.vacationsList) {
      this.displayed = this.vacationsList[this.index - 1];
      this.index = this.index - 1;
    }
  }

  async forward() {
    if (this.index != undefined && this.index != this.vacationsList.length - 1 && this.vacationsList) {
      this.displayed = this.vacationsList[this.index + 1];
      this.index = this.index + 1;
    }
  }

  async view(id: number) {
    await this._router.navigate([`/itinerary-viewer/${id}`]);
  }

  openImagePicker() {
    const inputElement = document.createElement('input');
    inputElement.type = 'file';
    inputElement.accept = 'image/*';
    inputElement.addEventListener('change', (event) => this.handleImageInput(event));
    inputElement.click();
  }

  async handleImageInput(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();

      reader.onload = () => {
        const imageUrl = reader.result as string;
        const myAvatar = this.myAvatar.nativeElement as HTMLElement;
        myAvatar.style.backgroundImage = "url('" + imageUrl + "')";

        if (this.user != undefined && this.user.id != undefined) {
          db.users.update(
            this.user.id,
            {src: imageUrl}
          ).then(function (updated) {
            if (!updated) {
              console.log("Nothing was updated.");
            }
          });
        }
      };

      reader.readAsDataURL(file);
    }
  }
}
