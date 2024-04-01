import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CitiesFilterService {

  constructor() {
  }

  async filter(text: string) {
    if (text.length >= 2) {
      try {
        const response = await fetch(`http://localhost:8080/api/v1/relation-trip/filter-cities/${text}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        return response.json();
      } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return null;
      }
    }
    return null;
  }

  async inList(city:string) {
    try {
      const response = await fetch(`http://localhost:8080/api/v1/relation-trip/city-exists/${city}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      return response.json();
    } catch (error) {
      console.error('There was a problem with the fetch operation:', error);
      return false;
    }
  }
}
