import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CibleDetailComponent } from './cible-detail.component';

describe('Component Tests', () => {
  describe('Cible Management Detail Component', () => {
    let comp: CibleDetailComponent;
    let fixture: ComponentFixture<CibleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CibleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cible: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CibleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CibleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cible on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cible).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
