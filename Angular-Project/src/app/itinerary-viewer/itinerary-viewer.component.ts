import {Component} from '@angular/core';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatButtonModule} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {db} from "../db";

@Component({
  selector: 'app-itinerary-viewer',
  standalone: true,
  imports: [
    MatSidenavModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIcon
  ],
  templateUrl: './itinerary-viewer.component.html',
  styleUrl: './itinerary-viewer.component.css'
})

export class ItineraryViewerComponent {
  async add() {
    await db.itineraries.add({
      id: 0,
      userId: 1,
      "photo": "assets/images/california-tour.jpg",
      "name": "California Tour",
      "destination": "California",
      "season": null,
      "attendant": null,
      "distance": 0,
      "poisNumber": 5,
      "highlights": [
        "Explore the vibrant art scene in California",
        "Immerse yourself in the breathtaking natural landscapes",
        "Embark on exhilarating hiking adventures"
      ],
      "pois": [
        {
          "name": "Yosemite National Park",
          "photo": "assets/images/california-tour.jpg",
          "description": "Yosemite National Park is renowned for its granite cliffs, waterfalls, clear streams, and giant sequoias. It offers a perfect blend of nature and hiking opportunities.",
          "longitude": -119.5383294,
          "latitude": 37.8651011,
          "tags": ["nature", "hiking"],
          "stars": 4.8,
          "address": "California, United States",
          "phone": "+1 209-372-0200",
          "website": "https://www.nps.gov/yose/index.htm",
          "schedule": [
            "Monday: Open 24 hours",
            "Tuesday: Open 24 hours",
            "Wednesday: Open 24 hours",
            "Thursday: Open 24 hours",
            "Friday: Open 24 hours",
            "Saturday: Open 24 hours",
            "Sunday: Open 24 hours"
          ]
        },
        {
          "name": "The Getty",
          "photo": "assets/images/california-tour.jpg",
          "description": "The Getty Center is a renowned art museum and architectural masterpiece, featuring an impressive collection of European paintings, sculptures, and decorative arts.",
          "longitude": -118.4740954,
          "latitude": 34.07803579999999,
          "tags": ["art", "nature"],
          "stars": 4.8,
          "address": "1200 Getty Center Dr, Los Angeles, CA 90049, United States",
          "phone": "+1 310-440-7300",
          "website": "https://www.getty.edu/visit/center/",
          "schedule": [
            "Monday: Closed",
            "Tuesday: 10:00 AM – 5:30 PM",
            "Wednesday: 10:00 AM – 5:30 PM",
            "Thursday: 10:00 AM – 5:30 PM",
            "Friday: 10:00 AM – 5:30 PM",
            "Saturday: 10:00 AM – 8:00 PM",
            "Sunday: 10:00 AM – 5:30 PM"
          ]
        },
        {
          "name": "Big Sur",
          "photo": "assets/images/california-tour.jpg",
          "description": "Big Sur is a rugged stretch of California's central coast, known for its dramatic cliffs, stunning ocean views, and numerous hiking trails that lead to hidden coves and waterfalls.",
          "longitude": -121.8080556,
          "latitude": 36.2704233,
          "tags": ["nature", "hiking"],
          "stars": -1,
          "address": "Big Sur, CA, USA",
          "phone": "",
          "website": "",
          "schedule": []
        },
        {
          "name": "The Broad",
          "photo": "assets/images/california-tour.jpg",
          "description": "The Broad is a contemporary art museum in downtown Los Angeles, showcasing an extensive collection of modern and contemporary art, including works by renowned artists.",
          "longitude": -118.2501802,
          "latitude": 34.0545021,
          "tags": ["art"],
          "stars": 4.7,
          "address": "221 S Grand Ave, Los Angeles, CA 90012, United States",
          "phone": "+1 213-232-6200",
          "website": "https://www.thebroad.org/",
          "schedule": [
            "Monday: Closed",
            "Tuesday: 11:00 AM – 5:00 PM",
            "Wednesday: 11:00 AM – 5:00 PM",
            "Thursday: 11:00 AM – 8:00 PM",
            "Friday: 11:00 AM – 5:00 PM",
            "Saturday: 10:00 AM – 6:00 PM",
            "Sunday: 10:00 AM – 6:00 PM"
          ]
        },
        {
          "name": "Lake Tahoe",
          "photo": "assets/images/california-tour.jpg",
          "description": "Lake Tahoe is a picturesque alpine lake surrounded by snow-capped peaks, offering a paradise for nature lovers and hikers with its crystal-clear waters and scenic trails.",
          "longitude": -120.0323507,
          "latitude": 39.0968493,
          "tags": ["nature", "hiking"],
          "stars": 4.8,
          "address": "Lake Tahoe, United States",
          "phone": "",
          "website": "",
          "schedule": []
        }
      ]
    })
  }
}
