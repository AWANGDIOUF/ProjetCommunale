import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CalendrierEvenementService } from '../service/calendrier-evenement.service';

import { CalendrierEvenementComponent } from './calendrier-evenement.component';

describe('CalendrierEvenement Management Component', () => {
  let comp: CalendrierEvenementComponent;
  let fixture: ComponentFixture<CalendrierEvenementComponent>;
  let service: CalendrierEvenementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CalendrierEvenementComponent],
    })
      .overrideTemplate(CalendrierEvenementComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CalendrierEvenementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CalendrierEvenementService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.calendrierEvenements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
