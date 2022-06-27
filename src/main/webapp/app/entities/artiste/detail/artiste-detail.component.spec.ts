import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArtisteDetailComponent } from './artiste-detail.component';

describe('Artiste Management Detail Component', () => {
  let comp: ArtisteDetailComponent;
  let fixture: ComponentFixture<ArtisteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArtisteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ artiste: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArtisteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArtisteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load artiste on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.artiste).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
