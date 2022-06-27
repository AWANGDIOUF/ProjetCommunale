import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvenementService } from '../service/evenement.service';
import { IEvenement, Evenement } from '../evenement.model';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

import { EvenementUpdateComponent } from './evenement-update.component';

describe('Evenement Management Update Component', () => {
  let comp: EvenementUpdateComponent;
  let fixture: ComponentFixture<EvenementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evenementService: EvenementService;
  let artisteService: ArtisteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvenementUpdateComponent],
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
      .overrideTemplate(EvenementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvenementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evenementService = TestBed.inject(EvenementService);
    artisteService = TestBed.inject(ArtisteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call artiste query and add missing value', () => {
      const evenement: IEvenement = { id: 456 };
      const artiste: IArtiste = { id: 31076 };
      evenement.artiste = artiste;

      const artisteCollection: IArtiste[] = [{ id: 56769 }];
      jest.spyOn(artisteService, 'query').mockReturnValue(of(new HttpResponse({ body: artisteCollection })));
      const expectedCollection: IArtiste[] = [artiste, ...artisteCollection];
      jest.spyOn(artisteService, 'addArtisteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      expect(artisteService.query).toHaveBeenCalled();
      expect(artisteService.addArtisteToCollectionIfMissing).toHaveBeenCalledWith(artisteCollection, artiste);
      expect(comp.artistesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evenement: IEvenement = { id: 456 };
      const artiste: IArtiste = { id: 77365 };
      evenement.artiste = artiste;

      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(evenement));
      expect(comp.artistesCollection).toContain(artiste);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evenement>>();
      const evenement = { id: 123 };
      jest.spyOn(evenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(evenementService.update).toHaveBeenCalledWith(evenement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evenement>>();
      const evenement = new Evenement();
      jest.spyOn(evenementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenement }));
      saveSubject.complete();

      // THEN
      expect(evenementService.create).toHaveBeenCalledWith(evenement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evenement>>();
      const evenement = { id: 123 };
      jest.spyOn(evenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evenementService.update).toHaveBeenCalledWith(evenement);
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
