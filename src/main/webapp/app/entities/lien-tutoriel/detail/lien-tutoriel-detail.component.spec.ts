import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LienTutorielDetailComponent } from './lien-tutoriel-detail.component';

describe('LienTutoriel Management Detail Component', () => {
  let comp: LienTutorielDetailComponent;
  let fixture: ComponentFixture<LienTutorielDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LienTutorielDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lienTutoriel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LienTutorielDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LienTutorielDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lienTutoriel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lienTutoriel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
