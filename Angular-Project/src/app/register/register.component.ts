import {Component, ViewEncapsulation} from '@angular/core';
import {MatFormField, MatLabel, MatOption, MatSelect} from '@angular/material/select';
import {MatInput, MatInputModule} from '@angular/material/input';
import {NgOptimizedImage} from "@angular/common";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from "@angular/material/datepicker";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonToggle} from "@angular/material/button-toggle";
import {MatButton} from "@angular/material/button";
import {Moment} from 'moment';
import {provideMomentDateAdapter} from "@angular/material-moment-adapter";
import {db} from "../db";
import {MatSnackBar} from "@angular/material/snack-bar";
import {EncrDecrService} from "../services/EncrDecrService/encr-decr.service";
import {MatActionList, MatListItem, MatListItemTitle, MatNavList} from "@angular/material/list";
import {Router} from "@angular/router";

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
    MatButton,
    MatActionList,
    MatListItem,
    MatNavList,
    MatListItemTitle
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
  username = new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(29), Validators.pattern("^[A-Za-z][A-Za-z0-9_.]{8,29}$")]);
  location = new FormControl('', [Validators.required])
  yearOfBirth = new FormControl('');
  password = new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(29), Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}")]);
  gender = new FormControl('');

  registerState = true

  constructor(private _snackBar: MatSnackBar, private _encrDecrService: EncrDecrService, private _router: Router) {
    if (window.localStorage.getItem("username") != null || window.localStorage.getItem("username") != undefined) {
      this._router.navigate(['/home']);
    }
  }

  async submitForm() {
    if (this.registerState) {
      let userFound = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: this.username.value});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });

      if (userFound != undefined) {
        this._snackBar.open("Username already exists", "Ok", {duration: 5000});
        return;
      }

      userFound = await db.transaction('r', [db.users], async () => {
        return db.users.get({email: this.email.value});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });

      if (userFound != undefined) {
        this._snackBar.open("Email already exists", "Ok", {duration: 5000});
        return;
      }

      if (this.username.valid && this.gender.valid && this.password.valid &&
        this.location.valid && this.yearOfBirth.valid && this.email.valid) {

        let newPassword = ""
        if (this.password.value != null) {
          newPassword = this._encrDecrService.encrypt(this.password.value)
        }

        await db.users.add({
          username: this.username.value != null ? this.username.value : '',
          location: this.location.value != null ? this.location.value : '',
          gender: this.gender.value != "" && this.gender.value != null ? this.gender.value : null,
          yearOfBirth: this.yearOfBirth.value != '' && this.yearOfBirth.value != null ? parseInt(this.yearOfBirth.value) : null,
          email: this.email.value != null ? this.email.value : '',
          password: newPassword,
          src: "assets/images/no-avatar.png"
        })

        this._snackBar.open("Your account has been created. Welcome!", "Ok", {duration: 5000})
      }
    } else {
      let usernameFound = await db.transaction('r', [db.users], async () => {
        return db.users.get({username: this.username.value});
      }).catch(error => {
        console.error("Error occurred while fetching user:", error);
      });

      if (usernameFound == undefined || this._encrDecrService.decrypt(usernameFound.password) != this.password.value) {
        this._snackBar.open("Incorrect username or password", "Ok", {duration: 5000});
        return;
      }

      window.localStorage.setItem("username", this.username.value != null ? this.username.value : "")
      await this._router.navigate(['/home']);

      this.registerState = true
    }
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

    if (this.password.hasError('maxlength')) {
      return "Must contain at most 29 characters"
    }

    return this.password.hasError("pattern") ? "Must contain number, uppercase and lowercase letter" : ''
  }

  getUsernameErrorMessage() {
    if (this.username.hasError('required')) {
      return 'You must enter a value';
    }

    if (this.username.hasError('minlength')) {
      return "Must contain at least 8 characters"
    }

    if (this.username.hasError('maxlength')) {
      return "Must contain at most 29 characters"
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

  changeState() {
    this.registerState = !this.registerState;
  }
}
