import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeVaccinDetailComponent } from './type-vaccin-detail.component';

describe('Component Tests', () => {
  describe('TypeVaccin Management Detail Component', () => {
    let comp: TypeVaccinDetailComponent;
    let fixture: ComponentFixture<TypeVaccinDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeVaccinDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeVaccin: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeVaccinDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeVaccinDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeVaccin on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeVaccin).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
