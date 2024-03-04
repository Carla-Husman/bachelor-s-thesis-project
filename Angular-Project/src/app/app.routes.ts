import { Routes } from '@angular/router';
import {RegisterComponent} from "./register/register.component";

export const routes: Routes = [
  { path: '', redirectTo: 'register', pathMatch: 'full' }, //default role
  { path: 'register', component: RegisterComponent }
];
