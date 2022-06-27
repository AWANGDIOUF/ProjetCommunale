import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartenairesDetailComponent } from './partenaires-detail.component';

describe('Partenaires Management Detail Component', () => {
  let comp: PartenairesDetailComponent;
  let fixture: ComponentFixture<PartenairesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartenairesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ partenaires: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartenairesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartenairesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load partenaires on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.partenaires).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
