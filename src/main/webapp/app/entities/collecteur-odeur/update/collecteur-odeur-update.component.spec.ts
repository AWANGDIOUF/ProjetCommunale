import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CollecteurOdeurService } from '../service/collecteur-odeur.service';
import { ICollecteurOdeur, CollecteurOdeur } from '../collecteur-odeur.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { CollecteurOdeurUpdateComponent } from './collecteur-odeur-update.component';

describe('CollecteurOdeur Management Update Component', () => {
  let comp: CollecteurOdeurUpdateComponent;
  let fixture: ComponentFixture<CollecteurOdeurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collecteurOdeurService: CollecteurOdeurService;
  let quartierService: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CollecteurOdeurUpdateComponent],
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
      .overrideTemplate(CollecteurOdeurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollecteurOdeurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collecteurOdeurService = TestBed.inject(CollecteurOdeurService);
    quartierService = TestBed.inject(QuartierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quartier query and add missing value', () => {
      const collecteurOdeur: ICollecteurOdeur = { id: 456 };
      const quartier: IQuartier = { id: 37558 };
      collecteurOdeur.quartier = quartier;

      const quartierCollection: IQuartier[] = [{ id: 40318 }];
      jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
      const additionalQuartiers = [quartier];
      const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
      jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collecteurOdeur });
      comp.ngOnInit();

      expect(quartierService.query).toHaveBeenCalled();
      expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
      expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const collecteurOdeur: ICollecteurOdeur = { id: 456 };
      const quartier: IQuartier = { id: 37053 };
      collecteurOdeur.quartier = quartier;

      activatedRoute.data = of({ collecteurOdeur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(collecteurOdeur));
      expect(comp.quartiersSharedCollection).toContain(quartier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollecteurOdeur>>();
      const collecteurOdeur = { id: 123 };
      jest.spyOn(collecteurOdeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collecteurOdeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collecteurOdeur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(collecteurOdeurService.update).toHaveBeenCalledWith(collecteurOdeur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollecteurOdeur>>();
      const collecteurOdeur = new CollecteurOdeur();
      jest.spyOn(collecteurOdeurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collecteurOdeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collecteurOdeur }));
      saveSubject.complete();

      // THEN
      expect(collecteurOdeurService.create).toHaveBeenCalledWith(collecteurOdeur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollecteurOdeur>>();
      const collecteurOdeur = { id: 123 };
      jest.spyOn(collecteurOdeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collecteurOdeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collecteurOdeurService.update).toHaveBeenCalledWith(collecteurOdeur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuartierById', () => {
      it('Should return tracked Quartier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuartierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
