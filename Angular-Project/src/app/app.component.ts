import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {DBConfig, NgxIndexedDBModule} from 'ngx-indexed-db';
import {EncrDecrService} from "./services/EncrDecrService/encr-decr.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  providers: [EncrDecrService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'RelationTrip';
}
