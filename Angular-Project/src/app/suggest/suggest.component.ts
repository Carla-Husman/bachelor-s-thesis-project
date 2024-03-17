import {Component, OnInit} from '@angular/core';
import {
  MatStepperModule,
  StepperOrientation
} from "@angular/material/stepper";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable, of} from "rxjs";
import {map, startWith} from 'rxjs/operators';
import {BreakpointObserver} from "@angular/cdk/layout";
import {AsyncPipe} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import {MatOption, MatSelect} from "@angular/material/select";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MatAutocompleteModule} from '@angular/material/autocomplete';

@Component({
  selector: 'app-suggest',
  standalone: true,
  imports: [
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    AsyncPipe,
    MatIconModule,
    MatSelect,
    MatOption,
    HttpClientModule,
    MatAutocompleteModule
  ],
  templateUrl: './suggest.component.html',
  styleUrl: './suggest.component.css',
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {displayDefaultIndicatorType: false},
    },
  ]
})
export class SuggestComponent implements OnInit {
  firstFormGroup = this._formBuilder.group({
    startingPoint: ['', Validators.required],
    age: [],
    gender: ['']
  });
  secondFormGroup = this._formBuilder.group({
    attendant: [''],
    season: [''],
    transport: [''],
    budget: ['']
  });
  thirdFormGroup = this._formBuilder.group({
    destination: [''],
  });
  fourthFormGroup = this._formBuilder.group({
    tags: ['', Validators.required],
    option: ['']
  });
  stepperOrientation: Observable<StepperOrientation>;
  isOptional = true;
  isEditable = true;
  attendant = ['Family', 'Friends', 'Couple', 'Single'];
  budget = ['Cheap', 'Affordable', 'Medium', 'Midrange', 'Expensive', 'Luxury'];
  transport = ['Car', 'Bus', 'Train', 'Plane', 'Boat', 'Bicycle', 'Motorcycle', 'Walking'];
  gender = ['Female', 'Male', 'Other'];
  interests = [
    'Adventure', 'Aquarium', 'Architecture', 'Art', 'Bar', 'Beach', 'Books', 'Camping',
    'Cinema', 'Concert', 'Cultural', 'Fishing', 'Food', 'Gallery', 'Hiking', 'History',
    'Hunting', 'Library', 'Mountain', 'Movies', 'Museum', 'Music', 'Nature', 'Park',
    'Photography', 'Relaxing', 'Religious', 'Restaurant', 'Romantic', 'Shopping',
    'Skiing', 'Theatre', 'Wildlife', 'Zoo'
  ];
  season = ['Spring', 'Summer', 'Autumn', 'Winter'];
  destination: string [] = []
  filteredDestination: Observable<string[]> | undefined;

  constructor(private _formBuilder: FormBuilder, private _http: HttpClient, breakpointObserver: BreakpointObserver) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  async ngOnInit() {
    let data = await this._http.get('assets/files/city_country_test.txt', {responseType: 'text'}).toPromise();
    if (data != undefined) {
      const linii = data.split('\n');
      this.destination = linii.map(linie => linie.trim());
      this.filteredDestination = this.thirdFormGroup.get('destination')?.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || '')),
      );
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.destination.filter(option => option.toLowerCase().includes(filterValue));
  }
}

