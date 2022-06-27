import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EntrepreneurDetailComponent } from './entrepreneur-detail.component';

describe('Entrepreneur Management Detail Component', () => {
  let comp: EntrepreneurDetailComponent;
  let fixture: ComponentFixture<EntrepreneurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EntrepreneurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ entrepreneur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EntrepreneurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EntrepreneurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load entrepreneur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.entrepreneur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
