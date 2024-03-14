import {Component, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {EncrDecrService} from "./services/EncrDecrService/encr-decr.service";
import {MatToolbar, MatToolbarModule} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatTabLink} from "@angular/material/tabs";
import {NgOptimizedImage} from "@angular/common";

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
    MatTabLink,
    NgOptimizedImage
  ],
  providers: [EncrDecrService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'RelationTrip';

  constructor() {
  }

  ngOnInit(): void {
  }

  isLoggedIn(): boolean {
    return !!window.localStorage.getItem('username');
  }
}
