import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArchiveSportDetailComponent } from './archive-sport-detail.component';

describe('Component Tests', () => {
  describe('ArchiveSport Management Detail Component', () => {
    let comp: ArchiveSportDetailComponent;
    let fixture: ComponentFixture<ArchiveSportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ArchiveSportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ archiveSport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ArchiveSportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArchiveSportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load archiveSport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.archiveSport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
