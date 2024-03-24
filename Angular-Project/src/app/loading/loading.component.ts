import {Component, Inject} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [
    MatProgressSpinner
  ],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { startingPoint: string }) {
  }
}
