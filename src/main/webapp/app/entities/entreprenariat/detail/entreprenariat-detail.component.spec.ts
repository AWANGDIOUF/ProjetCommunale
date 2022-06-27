import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EntreprenariatDetailComponent } from './entreprenariat-detail.component';

describe('Entreprenariat Management Detail Component', () => {
  let comp: EntreprenariatDetailComponent;
  let fixture: ComponentFixture<EntreprenariatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EntreprenariatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ entreprenariat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EntreprenariatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EntreprenariatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load entreprenariat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.entreprenariat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
