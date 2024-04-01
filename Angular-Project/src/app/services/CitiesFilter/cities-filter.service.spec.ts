import { TestBed } from '@angular/core/testing';

import { CitiesFilterService } from './cities-filter.service';

describe('CitiesFilterService', () => {
  let service: CitiesFilterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CitiesFilterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
