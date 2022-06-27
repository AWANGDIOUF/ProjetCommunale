import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResultatExamenService } from '../service/resultat-examen.service';
import { IResultatExamen, ResultatExamen } from '../resultat-examen.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { ResultatExamenUpdateComponent } from './resultat-examen-update.component';

describe('ResultatExamen Management Update Component', () => {
  let comp: ResultatExamenUpdateComponent;
  let fixture: ComponentFixture<ResultatExamenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resultatExamenService: ResultatExamenService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ResultatExamenUpdateComponent],
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
      .overrideTemplate(ResultatExamenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResultatExamenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resultatExamenService = TestBed.inject(ResultatExamenService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etablissement query and add missing value', () => {
      const resultatExamen: IResultatExamen = { id: 456 };
      const etablissement: IEtablissement = { id: 79849 };
      resultatExamen.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 63334 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resultatExamen });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resultatExamen: IResultatExamen = { id: 456 };
      const etablissement: IEtablissement = { id: 83731 };
      resultatExamen.etablissement = etablissement;

      activatedRoute.data = of({ resultatExamen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(resultatExamen));
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResultatExamen>>();
      const resultatExamen = { id: 123 };
      jest.spyOn(resultatExamenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultatExamen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resultatExamen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(resultatExamenService.update).toHaveBeenCalledWith(resultatExamen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResultatExamen>>();
      const resultatExamen = new ResultatExamen();
      jest.spyOn(resultatExamenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultatExamen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resultatExamen }));
      saveSubject.complete();

      // THEN
      expect(resultatExamenService.create).toHaveBeenCalledWith(resultatExamen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResultatExamen>>();
      const resultatExamen = { id: 123 };
      jest.spyOn(resultatExamenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultatExamen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resultatExamenService.update).toHaveBeenCalledWith(resultatExamen);
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
