import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {DBConfig, NgxIndexedDBModule} from 'ngx-indexed-db';

const dbConfig: DBConfig = {
  name: 'RelationTripDataBase',
  version: 1,
  objectStoresMeta: [{
    store: 'users',
    storeConfig: { keyPath: 'id', autoIncrement: true },
    storeSchema: [
      { name: 'username', keypath: 'username', options: { unique: true } },
      { name: 'email', keypath: 'email', options: { unique:  true} },
      { name: 'password', keypath: 'password', options: { unique:  false} },
      { name: 'gender', keypath: 'gender', options: { unique:  false} },
      { name: 'location', keypath: 'location', options: { unique:  false} },
      { name: 'yearOfBirth', keypath: 'yearOfBirth', options: { unique:  false} }
    ]
  }]
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'RelationTrip';
}
