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
import {db, Users} from "../../../../../Angular-Project/src/app/db";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {SlickCarouselModule} from "ngx-slick-carousel";
import {NgForOf, NgOptimizedImage} from "@angular/common";
import {MatActionList} from "@angular/material/list";
import {MatDivider} from "@angular/material/divider";
import {VacationsList} from "../models/VacationsList/vacations-list.model";

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
    MatDivider
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  user: Users | undefined | void
  vacationsList : VacationsList[] = []
  numberOfItineraries = 0
  option = 1

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

      for (let i=0; i<10; ++i){
        this.vacationsList.push(new VacationsList(1, "Calatorie1", "Destinatie", 5, "assets/images/imagine-1.jpeg", 23))
      }

    } else {
      await this._router.navigate(["/account"]);
    }
  }

  async logout() {
    localStorage.removeItem('username')
    await this._router.navigate(['/account'])
  }

  changeOption(newOption : number) {
    this.option = newOption;
  }

  async viewItinerary(id: number) {
    await this._router.navigate(['/home'])
  }
}
