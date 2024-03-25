import {Component, Inject} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [
    MatProgressSpinner
  ],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {
  firstArray: string[] = [
    "Hawaii",
    "London",
    "Tokyo",
    "Paris",
    "Lima",
    "Moscow",
    "Sydney",
    "Dubai",
    "Kyoto",
    "Singapore",
    "Berlin",
    "Brasilia",
    "Toronto",
    "Istanbul",
    "Rome"
  ];
  secondArray: string[] = [
    "Shanghai",
    "Busan",
    "Mumbai",
    "Bangkok",
    "Barcelona",
    "Amsterdam",
    "Vienna",
    "Athens",
    "Cairo",
    "Atena",
    "Jeju",
    "Beijing",
    "Seoul",
    "Budapest",
    "Prague"
  ];
  thirdArray: string[] = [
    "Argentina",
    "Madrid",
    "Bucharest",
    "Osaka",
    "Dublin",
    "Stockholm",
    "Helsinki",
    "Jakarta",
    "Warsaw",
    "Lisbon",
    "Dubrovnik",
    "Vancouver",
    "Zurich",
    "Edinburgh",
    "Hanoi"
  ];

  firstRandomValue: string;
  secondRandomValue: string;
  thirdRandomValue: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { startingPoint: string }) {
    this.firstRandomValue = this.getRandomValue(this.firstArray);
    this.secondRandomValue = this.getRandomValue(this.secondArray);
    this.thirdRandomValue = this.getRandomValue(this.thirdArray);
  }

  getRandomValue(array: any[]): string {
    return array[Math.floor(Math.random() * array.length)];
  }
}
