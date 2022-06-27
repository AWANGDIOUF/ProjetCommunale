import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EnsegnantDetailComponent } from './ensegnant-detail.component';

describe('Ensegnant Management Detail Component', () => {
  let comp: EnsegnantDetailComponent;
  let fixture: ComponentFixture<EnsegnantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnsegnantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ensegnant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EnsegnantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EnsegnantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ensegnant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ensegnant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
