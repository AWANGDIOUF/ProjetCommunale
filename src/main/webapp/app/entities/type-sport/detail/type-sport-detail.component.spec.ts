import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeSportDetailComponent } from './type-sport-detail.component';

describe('Component Tests', () => {
  describe('TypeSport Management Detail Component', () => {
    let comp: TypeSportDetailComponent;
    let fixture: ComponentFixture<TypeSportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeSportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeSport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeSportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeSportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeSport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeSport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
