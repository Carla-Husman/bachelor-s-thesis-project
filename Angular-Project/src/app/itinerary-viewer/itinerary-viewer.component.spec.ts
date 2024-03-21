import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItineraryViewerComponent } from './itinerary-viewer.component';

describe('ItineraryViewerComponent', () => {
  let component: ItineraryViewerComponent;
  let fixture: ComponentFixture<ItineraryViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItineraryViewerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItineraryViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
