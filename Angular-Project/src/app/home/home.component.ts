import {Component, OnInit} from '@angular/core';
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
import {db, Users} from "../db";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {NgForOf, NgOptimizedImage} from "@angular/common";
import {MatActionList} from "@angular/material/list";

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
    NgOptimizedImage
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  user: Users | undefined | void
  vacationsList = [
    { title: 'Călătorie 1', src: 'assets/images/imagine-1.jpeg', description: ""},
    { title: 'Călătorie 2', src: 'assets/images/imagine-2.jpg', description: "" },
    { title: 'Călătorie 3', src: 'assets/images/imagine-3.jpg', description: "" }
  ];
  index = 0;
  constructor(private _router: Router) {}

  async ngOnInit() {
    if (localStorage.getItem('username') != undefined || localStorage.getItem('username') != null) {
      this.user = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: localStorage.getItem('username')});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });

      if (this.user == undefined) {
        await this._router.navigate(["/account"]);
        return;
      }
    } else {
      await this._router.navigate(["/account"]);
    }
  }

  onPrev(){
    if (this.index > 0) {
      this.index--;
    }
  }

  onNext(){
    if(this.index < this.vacationsList.length - 1) {
      this.index++;
    }
  }
}
