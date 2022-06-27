import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DemandeInterviewService } from '../service/demande-interview.service';

import { DemandeInterviewComponent } from './demande-interview.component';

describe('DemandeInterview Management Component', () => {
  let comp: DemandeInterviewComponent;
  let fixture: ComponentFixture<DemandeInterviewComponent>;
  let service: DemandeInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DemandeInterviewComponent],
    })
      .overrideTemplate(DemandeInterviewComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeInterviewComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DemandeInterviewService);

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
    expect(comp.demandeInterviews?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
