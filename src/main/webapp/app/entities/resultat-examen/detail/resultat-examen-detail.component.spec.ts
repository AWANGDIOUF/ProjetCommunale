import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResultatExamenDetailComponent } from './resultat-examen-detail.component';

describe('ResultatExamen Management Detail Component', () => {
  let comp: ResultatExamenDetailComponent;
  let fixture: ComponentFixture<ResultatExamenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResultatExamenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resultatExamen: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResultatExamenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResultatExamenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resultatExamen on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resultatExamen).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
