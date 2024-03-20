import {Component, inject, Input, OnInit, ViewChild} from '@angular/core';
import {MatStepper, MatStepperModule, StepperOrientation} from "@angular/material/stepper";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormArray, FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {map, startWith} from 'rxjs/operators';
import {BreakpointObserver} from "@angular/cdk/layout";
import {AsyncPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import {MatOption, MatSelect} from "@angular/material/select";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from "@angular/material/card";
import {db, Users} from "../db";
import {
  MatChipEditedEvent,
  MatChipInputEvent,
  MatChipsModule
} from "@angular/material/chips";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatExpansionPanelDescription} from "@angular/material/expansion";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-suggest',
  standalone: true,
  imports: [
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    AsyncPipe,
    MatSelect,
    MatOption,
    HttpClientModule,
    MatAutocompleteModule,
    MatCardModule,
    NgOptimizedImage,
    NgIf,
    MatFormFieldModule,
    MatChipsModule,
    MatIconModule,
    NgForOf,
    MatExpansionPanelDescription
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
    startingPoint: ['', [Validators.required, Validators.pattern('^[a-zA-ZÀ-ÿĀ-žǍ-ȳ\'’.`\\s]+,\\s?[a-zA-ZÀ-ÿĀ-žǍ-ȳ\'’.`\\s]+$')]],
    age: ['', [Validators.min(18), Validators.max(100)]],
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
    tags: this._formBuilder.array([], Validators.required),
  });
  stepperOrientation: Observable<StepperOrientation>;
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
  user: Users | undefined | void
  thisYear = new Date().getFullYear()

  constructor(private _formBuilder: FormBuilder, private _http: HttpClient, breakpointObserver: BreakpointObserver) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'vertical' : 'vertical')));
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

    if (window.localStorage.getItem('username') != undefined || window.localStorage.getItem('username') != null) {
      this.user = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: window.localStorage.getItem('username')});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });
    }

    if (this.user != undefined) {
      this.firstFormGroup.setValue({
        startingPoint: this.user.location,
        age: this.user.yearOfBirth != null ? (this.thisYear - this.user.yearOfBirth).toString() : '',
        gender: this.user.gender != null ? this.user.gender : ''
      });
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.destination.filter(option => option.toLowerCase().includes(filterValue));
  }

  onSubmit() {
    console.log(this.firstFormGroup.get('startingPoint')?.value)
    console.log(this.firstFormGroup.get('age')?.value)
    console.log(this.firstFormGroup.get('gender')?.value)
    console.log(this.secondFormGroup.get('attendant')?.value)
    console.log(this.secondFormGroup.get('transport')?.value)
    console.log(this.secondFormGroup.get('season')?.value)
    console.log(this.secondFormGroup.get('budget')?.value)
    console.log(this.thirdFormGroup.get('destination')?.value)
    console.log(this.fourthFormGroup.get('tags')?.value)
    console.log(this.optionalTags)
  }

  @ViewChild('stepper') stepper!: MatStepper;
  startingPointHasError = "";

  onNextOne() {
    const startingPointValue = this.firstFormGroup.get('startingPoint')?.value;
    const startingPointPattern = /^[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\s]+,\s?[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\s]+$/;
    console.log(startingPointValue)
    if (!this.firstFormGroup.valid) {
      if (this.firstFormGroup.get('startingPoint')?.value == "") {
        this.startingPointHasError = "Enter your location";
      } else if (!startingPointPattern.test(<string>startingPointValue)) {
        this.startingPointHasError = "Format is City, Country"
      }
    } else {
      this.stepper.next();
      this.startingPointHasError = "";
    }
  }

  destinationHasError = "";

  onNextThree() {
    if (!this.destination.includes(<string>this.thirdFormGroup.get("destination")?.value)) {
      this.destinationHasError = "Destination must be from list"
    } else {
      this.destinationHasError = ""
      this.stepper.next()
    }
  }

  tagsHasError = ""

  onNextFourth() {
    const tagsArray = this.fourthFormGroup.get('tags') as FormArray;
    if (tagsArray.value.length == 0) {
      this.tagsHasError = "You need to select at least one tag";
    } else {
      this.tagsHasError = "";
      this.stepper.next();
    }
  }

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  optionalTags: string[] = [];

  announcer = inject(LiveAnnouncer);

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add tag
    if (value) {
      this.optionalTags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(optionalTag: string): void {
    const index = this.optionalTags.indexOf(optionalTag);

    if (index >= 0) {
      this.optionalTags.splice(index, 1);

      this.announcer.announce(`Removed ${optionalTag}`);
    }
  }

  edit(optionalTag: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    // Remove tag if it no longer has a name
    if (!value) {
      this.remove(optionalTag);
      return;
    }

    // Edit existing tag
    const index = this.optionalTags.indexOf(optionalTag);
    if (index >= 0) {
      this.optionalTags[index] = value;
    }
  }

  onSelectionChange(event: any) {
    const value = event.source.value;
    const tagsArray = this.fourthFormGroup.get('tags') as FormArray;
    const index = tagsArray.value.indexOf(value);

    if (index !== -1) {
      tagsArray.removeAt(index);
    } else {
      tagsArray.push(this._formBuilder.control(value));
    }
  }
}

