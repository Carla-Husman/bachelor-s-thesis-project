import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ItineraryService {
  private result: any = null;

  constructor() {
  }

  setResult(result: any) {
    this.result = result;
  }

  getResult() {
    return this.result;
  }
}
