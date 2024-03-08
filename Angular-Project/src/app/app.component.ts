import {Component, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {DBConfig, NgxIndexedDBModule} from 'ngx-indexed-db';
import {EncrDecrService} from "./services/EncrDecrService/encr-decr.service";
import {MatToolbar, MatToolbarModule} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatTabLink} from "@angular/material/tabs";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatToolbar,
    MatToolbarModule,
    MatIcon,
    MatTabLink
  ],
  providers: [EncrDecrService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit{
  title = 'RelationTrip';
  navBarActive = false;

  constructor() {
  }

  ngOnInit(): void {
    this.navBarActive = localStorage.getItem('username') != undefined || localStorage.getItem('username') != null;
  }
}
