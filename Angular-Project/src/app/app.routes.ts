import {Routes} from '@angular/router';
import {RegisterComponent} from "./register/register.component";
import {HomeComponent} from "./home/home.component";
import {SuggestComponent} from "./suggest/suggest.component";
import {ItineraryViewerComponent} from "./itinerary-viewer/itinerary-viewer.component";

export const routes: Routes = [
  {path: '', redirectTo: 'account', pathMatch: 'full'}, //default role
  {path: 'account', component: RegisterComponent},
  {path: 'home', component: HomeComponent},
  {path: 'suggest-itinerary', component: SuggestComponent},
  {path: 'itinerary-viewer/:id', component: ItineraryViewerComponent},
  {path: '**', redirectTo: 'account'}
];
