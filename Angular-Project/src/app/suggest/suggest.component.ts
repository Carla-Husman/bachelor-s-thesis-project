import {Component, inject, ViewEncapsulation, OnInit, ViewChild} from '@angular/core';
import {MatStepper, MatStepperModule, StepperOrientation} from "@angular/material/stepper";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormArray, FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable, switchMap} from "rxjs";
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
import {PlannerService} from "../services/Planner/planner.service";
import {MatDialog} from "@angular/material/dialog";
import {DialogComponent} from "../dialog/dialog.component";
import {LoadingComponent} from "../loading/loading.component";
import {Router} from "@angular/router";
import {ItineraryService} from "../services/SendItinerary/itinerary.service";
import {CitiesFilterService} from "../services/CitiesFilter/cities-filter.service";
import {scheduleReadableStreamLike} from "rxjs/internal/scheduled/scheduleReadableStreamLike";

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
  ],
  encapsulation: ViewEncapsulation.None
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
  readonly attendant = ['Family', 'Friends', 'Couple', 'Single'];
  readonly budget = ['Cheap', 'Affordable', 'Medium', 'Midrange', 'Expensive', 'Luxury'];
  readonly transport = ['Car', 'Bus', 'Train', 'Plane', 'Boat', 'Bicycle', 'Motorcycle', 'Walking'];
  readonly gender = ['Female', 'Male', 'Other'];
  readonly interests = [
    'Adventure', 'Aquarium', 'Architecture', 'Art', 'Bar', 'Beach', 'Books', 'Camping',
    'Cinema', 'Concert', 'Cultural', 'Fishing', 'Food', 'Gallery', 'Hiking', 'History',
    'Hunting', 'Library', 'Mountain', 'Movies', 'Museum', 'Music', 'Nature', 'Park',
    'Photography', 'Relaxing', 'Religious', 'Restaurant', 'Romantic', 'Shopping',
    'Skiing', 'Theatre', 'Wildlife', 'Zoo'
  ];
  readonly season = ['Spring', 'Summer', 'Autumn', 'Winter'];
  filteredDestination: Observable<string[]> | undefined;
  private _user: Users | undefined | void;
  private readonly _thisYear = new Date().getFullYear();

  constructor(private _formBuilder: FormBuilder, private _http: HttpClient, private _breakpointObserver: BreakpointObserver,
              private _plannerService: PlannerService, private _dialog: MatDialog, private _router: Router, private _itinerary: ItineraryService,
              private _citiesFilter: CitiesFilterService) {
    this.stepperOrientation = this._breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'vertical' : 'vertical')));
  }

  async ngOnInit() {
    this.filteredDestination = this.thirdFormGroup.get('destination')?.valueChanges.pipe(
      startWith(''),
      switchMap(value => this._filter(value || '')),
    );

    if (window.localStorage.getItem('username') != undefined || window.localStorage.getItem('username') != null) {
      this._user = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: window.localStorage.getItem('username')});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });
    }

    if (this._user != undefined) {
      this.firstFormGroup.setValue({
        startingPoint: this._user.location,
        age: this._user.yearOfBirth != null ? (this._thisYear - this._user.yearOfBirth).toString() : '',
        gender: this._user.gender != null ? this._user.gender.charAt(0).toUpperCase() + this._user.gender.slice(1) : ''
      });
    }
  }

  private async _filter(value: string) {
    let listOfCities: string[] = [];
    await this._citiesFilter.filter(value).then(response => {
      listOfCities = response;
    });
    return listOfCities;
  }

  async onSubmit() {
    const otherInterests = this.optionalTags.join(', ');

    const tags = this.fourthFormGroup.get('tags')?.value as string[];
    let interestss = [];
    if (Array.isArray(tags)) {
      for (let i = 0; i < tags?.length; ++i) {
        interestss.push(tags[i].toUpperCase());
      }
    }

    let planner_input = {
      destination: this.thirdFormGroup.get('destination')?.value,
      startingPoint: this.firstFormGroup.get('startingPoint')?.value,
      age: this.firstFormGroup.get('age')?.value == "" ? null : this.firstFormGroup.get('age')?.value,
      gender: this.firstFormGroup.get('gender')?.value == undefined || this.firstFormGroup.get('gender')?.value == "" ? null : this.firstFormGroup.get('gender')?.value?.toUpperCase(),
      attendant: this.secondFormGroup.get('attendant')?.value == "" ? null : this.secondFormGroup.get('attendant')?.value?.toUpperCase(),
      season: this.secondFormGroup.get('season')?.value == "" ? null : this.secondFormGroup.get('season')?.value?.toUpperCase(),
      transport: this.secondFormGroup.get('transport')?.value == "" ? null : this.secondFormGroup.get('transport')?.value?.toUpperCase(),
      budget: this.secondFormGroup.get('budget')?.value == "" ? null : this.secondFormGroup.get('budget')?.value?.toUpperCase(),
      interests: interestss,
      otherInterests: this.optionalTags.length == 0 ? null : otherInterests,
    };

    const dialogRef = this._dialog.open(LoadingComponent, {
      disableClose: true,
      data: {
        startingPoint: this.firstFormGroup.get('startingPoint')?.value
      }
    });

    this._plannerService.vacationPlanner(planner_input).then(async response => {
      dialogRef.close();

      if (response != null) {
        this._itinerary.setResult(response);
        await this._router.navigate(['/itinerary-viewer/-1']);
      } else {
        this._dialog.open(DialogComponent, {
          data: {
            title: 'Itinerary Generation Error',
            text: 'Sorry, but we encountered an issue generating your itinerary. ' +
              'Please try again later or contact our support team for assistance. ' +
              'Thank you for your understanding, and we apologize for any inconvenience caused.',
          },
        });
      }
    });
  }

  @ViewChild('stepper') stepper!: MatStepper;
  startingPointHasError = "";

  onNextOne() {
    const startingPointValue = this.firstFormGroup.get('startingPoint')?.value;
    const startingPointPattern = /^[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\s]+,\s?[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\s]+$/;

    if (!this.firstFormGroup.valid) {
      if (this.firstFormGroup.get('startingPoint')?.value == "") {
        this.startingPointHasError = "Enter your location";
      } else if (!startingPointPattern.test(<string>startingPointValue)) {
        this.startingPointHasError = "Format is City, Country";
      }
    } else {
      this.stepper.next();
      this.startingPointHasError = "";
    }
  }

  destinationHasError = "";

  async onNextThree() {
    if (this.thirdFormGroup.get("destination")?.value != "") {
      await this._citiesFilter.inList(<string>this.thirdFormGroup.get("destination")?.value).then(response => {
        if (response) {
          this.destinationHasError = "";
          this.stepper.next();
        } else {
          this.destinationHasError = "Your choice isn't in our database or  doesn't respect the format.";
        }
      });
    } else {
      this.destinationHasError = "";
      this.stepper.next();
    }
  }

  tagsHasError = "";

  onNextFourth() {
    const tagsArray = this.fourthFormGroup.get('tags') as FormArray;
    if (tagsArray.value.length == 0) {
      this.tagsHasError = "You need to select at least one tag";
    } else {
      this.tagsHasError = "";
      this.stepper.next();
    }
  }

  readonly addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  optionalTags: string[] = [];

  private _announcer = inject(LiveAnnouncer);

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add tag
    if (value) {
      this.optionalTags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  async remove(optionalTag: string) {
    const index = this.optionalTags.indexOf(optionalTag);

    if (index >= 0) {
      this.optionalTags.splice(index, 1);

      await this._announcer.announce(`Removed ${optionalTag}`);
    }
  }

  async edit(optionalTag: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    // Remove tag if it no longer has a name
    if (!value) {
      await this.remove(optionalTag);
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
