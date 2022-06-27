import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EleveurDetailComponent } from './eleveur-detail.component';

describe('Eleveur Management Detail Component', () => {
  let comp: EleveurDetailComponent;
  let fixture: ComponentFixture<EleveurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EleveurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eleveur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EleveurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EleveurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eleveur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eleveur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
