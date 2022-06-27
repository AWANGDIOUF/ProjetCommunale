import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecuperationRecyclableDetailComponent } from './recuperation-recyclable-detail.component';

describe('RecuperationRecyclable Management Detail Component', () => {
  let comp: RecuperationRecyclableDetailComponent;
  let fixture: ComponentFixture<RecuperationRecyclableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecuperationRecyclableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recuperationRecyclable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecuperationRecyclableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecuperationRecyclableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recuperationRecyclable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recuperationRecyclable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
