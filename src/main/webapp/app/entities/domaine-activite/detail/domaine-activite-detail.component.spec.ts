import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DomaineActiviteDetailComponent } from './domaine-activite-detail.component';

describe('DomaineActivite Management Detail Component', () => {
  let comp: DomaineActiviteDetailComponent;
  let fixture: ComponentFixture<DomaineActiviteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DomaineActiviteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ domaineActivite: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DomaineActiviteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DomaineActiviteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load domaineActivite on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.domaineActivite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
