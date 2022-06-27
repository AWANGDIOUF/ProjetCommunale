import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VidangeDetailComponent } from './vidange-detail.component';

describe('Vidange Management Detail Component', () => {
  let comp: VidangeDetailComponent;
  let fixture: ComponentFixture<VidangeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VidangeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vidange: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VidangeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VidangeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vidange on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vidange).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
