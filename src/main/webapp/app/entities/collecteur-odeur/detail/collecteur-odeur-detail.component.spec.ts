import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CollecteurOdeurDetailComponent } from './collecteur-odeur-detail.component';

describe('CollecteurOdeur Management Detail Component', () => {
  let comp: CollecteurOdeurDetailComponent;
  let fixture: ComponentFixture<CollecteurOdeurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CollecteurOdeurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ collecteurOdeur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CollecteurOdeurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CollecteurOdeurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load collecteurOdeur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.collecteurOdeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
