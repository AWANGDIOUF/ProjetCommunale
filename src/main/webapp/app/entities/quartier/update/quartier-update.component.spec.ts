import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuartierService } from '../service/quartier.service';
import { IQuartier, Quartier } from '../quartier.model';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

import { QuartierUpdateComponent } from './quartier-update.component';

describe('Quartier Management Update Component', () => {
  let comp: QuartierUpdateComponent;
  let fixture: ComponentFixture<QuartierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quartierService: QuartierService;
  let artisteService: ArtisteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuartierUpdateComponent],
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
      .overrideTemplate(QuartierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuartierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quartierService = TestBed.inject(QuartierService);
    artisteService = TestBed.inject(ArtisteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call artiste query and add missing value', () => {
      const quartier: IQuartier = { id: 456 };
      const artiste: IArtiste = { id: 5193 };
      quartier.artiste = artiste;

      const artisteCollection: IArtiste[] = [{ id: 74218 }];
      jest.spyOn(artisteService, 'query').mockReturnValue(of(new HttpResponse({ body: artisteCollection })));
      const expectedCollection: IArtiste[] = [artiste, ...artisteCollection];
      jest.spyOn(artisteService, 'addArtisteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quartier });
      comp.ngOnInit();

      expect(artisteService.query).toHaveBeenCalled();
      expect(artisteService.addArtisteToCollectionIfMissing).toHaveBeenCalledWith(artisteCollection, artiste);
      expect(comp.artistesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quartier: IQuartier = { id: 456 };
      const artiste: IArtiste = { id: 61791 };
      quartier.artiste = artiste;

      activatedRoute.data = of({ quartier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(quartier));
      expect(comp.artistesCollection).toContain(artiste);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quartier>>();
      const quartier = { id: 123 };
      jest.spyOn(quartierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quartier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quartier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(quartierService.update).toHaveBeenCalledWith(quartier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quartier>>();
      const quartier = new Quartier();
      jest.spyOn(quartierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quartier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quartier }));
      saveSubject.complete();

      // THEN
      expect(quartierService.create).toHaveBeenCalledWith(quartier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quartier>>();
      const quartier = { id: 123 };
      jest.spyOn(quartierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quartier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quartierService.update).toHaveBeenCalledWith(quartier);
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
