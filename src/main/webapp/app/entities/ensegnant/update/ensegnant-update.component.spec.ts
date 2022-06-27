import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EnsegnantService } from '../service/ensegnant.service';
import { IEnsegnant, Ensegnant } from '../ensegnant.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { EnsegnantUpdateComponent } from './ensegnant-update.component';

describe('Ensegnant Management Update Component', () => {
  let comp: EnsegnantUpdateComponent;
  let fixture: ComponentFixture<EnsegnantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ensegnantService: EnsegnantService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EnsegnantUpdateComponent],
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
      .overrideTemplate(EnsegnantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnsegnantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ensegnantService = TestBed.inject(EnsegnantService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etablissement query and add missing value', () => {
      const ensegnant: IEnsegnant = { id: 456 };
      const etablissement: IEtablissement = { id: 22629 };
      ensegnant.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 70969 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ensegnant });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ensegnant: IEnsegnant = { id: 456 };
      const etablissement: IEtablissement = { id: 90201 };
      ensegnant.etablissement = etablissement;

      activatedRoute.data = of({ ensegnant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ensegnant));
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ensegnant>>();
      const ensegnant = { id: 123 };
      jest.spyOn(ensegnantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ensegnant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ensegnant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ensegnantService.update).toHaveBeenCalledWith(ensegnant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ensegnant>>();
      const ensegnant = new Ensegnant();
      jest.spyOn(ensegnantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ensegnant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ensegnant }));
      saveSubject.complete();

      // THEN
      expect(ensegnantService.create).toHaveBeenCalledWith(ensegnant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ensegnant>>();
      const ensegnant = { id: 123 };
      jest.spyOn(ensegnantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ensegnant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ensegnantService.update).toHaveBeenCalledWith(ensegnant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEtablissementById', () => {
      it('Should return tracked Etablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtablissementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
