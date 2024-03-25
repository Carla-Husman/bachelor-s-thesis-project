import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PlannerService {

  constructor() {
  }

  async vacationPlanner(planner_input: {
    startingPoint: string | null | undefined;
    attendant: null | string | undefined;
    otherInterests: null | string;
    gender: null | string | undefined;
    destination: string | null | undefined;
    season: null | string | undefined;
    transport: null | string | undefined;
    interests: any[];
    age: string | null | undefined;
    budget: null | string | undefined;
  }) {
    try {
      const response = await fetch('http://localhost:8080/api/v1/test/temp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(planner_input),
      });
      return await response.json();
    } catch (error) {
      console.error('There was a problem with the fetch operation:', error);
      return null;
    }
  }
}
