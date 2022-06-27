import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DonDetailComponent } from './don-detail.component';

describe('Don Management Detail Component', () => {
  let comp: DonDetailComponent;
  let fixture: ComponentFixture<DonDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DonDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ don: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DonDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DonDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load don on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.don).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
