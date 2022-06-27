import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeInterviewDetailComponent } from './demande-interview-detail.component';

describe('DemandeInterview Management Detail Component', () => {
  let comp: DemandeInterviewDetailComponent;
  let fixture: ComponentFixture<DemandeInterviewDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandeInterviewDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demandeInterview: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemandeInterviewDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeInterviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demandeInterview on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demandeInterview).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
