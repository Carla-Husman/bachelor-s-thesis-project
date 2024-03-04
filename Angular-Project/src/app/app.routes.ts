import { Routes } from '@angular/router';
import {RegisterComponent} from "./register/register.component";
import {HomeComponent} from "./home/home.component";

export const routes: Routes = [
  { path: '', redirectTo: 'account', pathMatch: 'full' }, //default role
  { path: 'account', component: RegisterComponent },
  { path: 'home', component: HomeComponent},
  { path: '**', redirectTo: 'account' }
];
