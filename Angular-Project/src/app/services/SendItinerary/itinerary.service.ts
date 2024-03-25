import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ItineraryService {
  private result: any = null;
  private id: number = -1;

  constructor() {
  }

  setResult(result: any) {
    this.result = result;
  }

  getResult() {
    return this.result;
  }

  getId() {
    return this.id;
  }

  setId(id: number) {
    this.id = id;
  }
}
