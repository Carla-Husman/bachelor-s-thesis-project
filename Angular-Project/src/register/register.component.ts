import {Component, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel, MatOption, MatSelect} from '@angular/material/select';
import {MatInput, MatInputModule} from '@angular/material/input';
import {NgOptimizedImage} from "@angular/common";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatDatepicker, MatDatepickerModule, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonToggle} from "@angular/material/button-toggle";
import {MatButton} from "@angular/material/button";
import {Moment} from 'moment';
import {provideMomentDateAdapter} from "@angular/material-moment-adapter";

export const MY_FORMATS = {
  parse: {
    dateInput: 'YYYY',
  },
  display: {
    dateInput: 'YYYY',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
};

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgOptimizedImage,
    MatSlideToggle,
    MatOption,
    MatLabel,
    MatSelect,
    MatFormField,
    MatCardTitle,
    MatCardHeader,
    MatCard,
    MatCardContent,
    MatIcon,
    MatDatepickerToggle,
    MatDatepicker,
    MatRadioButton,
    MatRadioGroup,
    MatDatepickerInput,
    MatDatepickerModule,
    MatInput,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonToggle,
    MatButton
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  providers: [
    provideMomentDateAdapter(MY_FORMATS),
  ],
  encapsulation: ViewEncapsulation.None
})
export class RegisterComponent {
  email = new FormControl('', [Validators.required, Validators.email]);
  username = new FormControl('', [Validators.required, Validators.minLength(8), Validators.pattern("^[A-Za-z][A-Za-z0-9_]{8,29}$")]);
  location = new FormControl('', [Validators.required])
  yearOfBirth = new FormControl('');
  password = new FormControl('', [Validators.required, Validators.minLength(8), Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}")]);
  gender = new FormControl('');

  add(){
    //this.indexedDbService.add("username", "value")
  }
  getEmailErrorMessage() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  getPasswordErrorMessage() {
    if (this.password.hasError('required')) {
      return 'You must enter a value';
    }

    if (this.password.hasError('minlength')) {
      return "Must contain at least 8 characters";
    }

    return this.password.hasError("pattern")? "Must contain number, uppercase and lowercase letter" : ''
  }

  getUsernameErrorMessage() {
    if (this.username.hasError('required')) {
      return 'You must enter a value';
    }

    if (this.username.hasError('minlength')) {
      return "Must contain at least 8 characters"
    }

    return "Invalid username format";
  }

  getLocationErrorMessage() {
    if (this.location.hasError('required')) {
      return 'You must choose a location';
    }

    return "";
  }

  setYear(normalizedYear: Moment, datepicker: MatDatepicker<Moment>) {
      this.yearOfBirth.setValue(normalizedYear.year().toString());
      datepicker.close();
  }
}
