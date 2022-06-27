import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InterviewsArtisteService } from '../service/interviews-artiste.service';
import { IInterviewsArtiste, InterviewsArtiste } from '../interviews-artiste.model';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

import { InterviewsArtisteUpdateComponent } from './interviews-artiste-update.component';

describe('InterviewsArtiste Management Update Component', () => {
  let comp: InterviewsArtisteUpdateComponent;
  let fixture: ComponentFixture<InterviewsArtisteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interviewsArtisteService: InterviewsArtisteService;
  let artisteService: ArtisteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InterviewsArtisteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InterviewsArtisteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterviewsArtisteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interviewsArtisteService = TestBed.inject(InterviewsArtisteService);
    artisteService = TestBed.inject(ArtisteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call artiste query and add missing value', () => {
      const interviewsArtiste: IInterviewsArtiste = { id: 456 };
      const artiste: IArtiste = { id: 29951 };
      interviewsArtiste.artiste = artiste;

      const artisteCollection: IArtiste[] = [{ id: 20307 }];
      jest.spyOn(artisteService, 'query').mockReturnValue(of(new HttpResponse({ body: artisteCollection })));
      const expectedCollection: IArtiste[] = [artiste, ...artisteCollection];
      jest.spyOn(artisteService, 'addArtisteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ interviewsArtiste });
      comp.ngOnInit();

      expect(artisteService.query).toHaveBeenCalled();
      expect(artisteService.addArtisteToCollectionIfMissing).toHaveBeenCalledWith(artisteCollection, artiste);
      expect(comp.artistesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const interviewsArtiste: IInterviewsArtiste = { id: 456 };
      const artiste: IArtiste = { id: 60741 };
      interviewsArtiste.artiste = artiste;

      activatedRoute.data = of({ interviewsArtiste });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(interviewsArtiste));
      expect(comp.artistesCollection).toContain(artiste);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterviewsArtiste>>();
      const interviewsArtiste = { id: 123 };
      jest.spyOn(interviewsArtisteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interviewsArtiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interviewsArtiste }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(interviewsArtisteService.update).toHaveBeenCalledWith(interviewsArtiste);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterviewsArtiste>>();
      const interviewsArtiste = new InterviewsArtiste();
      jest.spyOn(interviewsArtisteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interviewsArtiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interviewsArtiste }));
      saveSubject.complete();

      // THEN
      expect(interviewsArtisteService.create).toHaveBeenCalledWith(interviewsArtiste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InterviewsArtiste>>();
      const interviewsArtiste = { id: 123 };
      jest.spyOn(interviewsArtisteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interviewsArtiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interviewsArtisteService.update).toHaveBeenCalledWith(interviewsArtiste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackArtisteById', () => {
      it('Should return tracked Artiste primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackArtisteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
