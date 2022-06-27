import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VainqueurDetailComponent } from './vainqueur-detail.component';

describe('Vainqueur Management Detail Component', () => {
  let comp: VainqueurDetailComponent;
  let fixture: ComponentFixture<VainqueurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VainqueurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vainqueur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VainqueurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VainqueurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vainqueur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vainqueur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
