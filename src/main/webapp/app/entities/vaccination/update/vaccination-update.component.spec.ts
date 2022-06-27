import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VaccinationService } from '../service/vaccination.service';
import { IVaccination, Vaccination } from '../vaccination.model';
import { ITypeVaccin } from 'app/entities/type-vaccin/type-vaccin.model';
import { TypeVaccinService } from 'app/entities/type-vaccin/service/type-vaccin.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

import { VaccinationUpdateComponent } from './vaccination-update.component';

describe('Vaccination Management Update Component', () => {
  let comp: VaccinationUpdateComponent;
  let fixture: ComponentFixture<VaccinationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vaccinationService: VaccinationService;
  let typeVaccinService: TypeVaccinService;
  let annonceService: AnnonceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VaccinationUpdateComponent],
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
      .overrideTemplate(VaccinationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaccinationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vaccinationService = TestBed.inject(VaccinationService);
    typeVaccinService = TestBed.inject(TypeVaccinService);
    annonceService = TestBed.inject(AnnonceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call typeVaccin query and add missing value', () => {
      const vaccination: IVaccination = { id: 456 };
      const typeVaccin: ITypeVaccin = { id: 43124 };
      vaccination.typeVaccin = typeVaccin;

      const typeVaccinCollection: ITypeVaccin[] = [{ id: 5614 }];
      jest.spyOn(typeVaccinService, 'query').mockReturnValue(of(new HttpResponse({ body: typeVaccinCollection })));
      const expectedCollection: ITypeVaccin[] = [typeVaccin, ...typeVaccinCollection];
      jest.spyOn(typeVaccinService, 'addTypeVaccinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      expect(typeVaccinService.query).toHaveBeenCalled();
      expect(typeVaccinService.addTypeVaccinToCollectionIfMissing).toHaveBeenCalledWith(typeVaccinCollection, typeVaccin);
      expect(comp.typeVaccinsCollection).toEqual(expectedCollection);
    });

    it('Should call Annonce query and add missing value', () => {
      const vaccination: IVaccination = { id: 456 };
      const annonce: IAnnonce = { id: 93681 };
      vaccination.annonce = annonce;

      const annonceCollection: IAnnonce[] = [{ id: 443 }];
      jest.spyOn(annonceService, 'query').mockReturnValue(of(new HttpResponse({ body: annonceCollection })));
      const additionalAnnonces = [annonce];
      const expectedCollection: IAnnonce[] = [...additionalAnnonces, ...annonceCollection];
      jest.spyOn(annonceService, 'addAnnonceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      expect(annonceService.query).toHaveBeenCalled();
      expect(annonceService.addAnnonceToCollectionIfMissing).toHaveBeenCalledWith(annonceCollection, ...additionalAnnonces);
      expect(comp.annoncesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vaccination: IVaccination = { id: 456 };
      const typeVaccin: ITypeVaccin = { id: 45853 };
      vaccination.typeVaccin = typeVaccin;
      const annonce: IAnnonce = { id: 29686 };
      vaccination.annonce = annonce;

      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vaccination));
      expect(comp.typeVaccinsCollection).toContain(typeVaccin);
      expect(comp.annoncesSharedCollection).toContain(annonce);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaccination>>();
      const vaccination = { id: 123 };
      jest.spyOn(vaccinationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccination }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vaccinationService.update).toHaveBeenCalledWith(vaccination);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaccination>>();
      const vaccination = new Vaccination();
      jest.spyOn(vaccinationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vaccination }));
      saveSubject.complete();

      // THEN
      expect(vaccinationService.create).toHaveBeenCalledWith(vaccination);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vaccination>>();
      const vaccination = { id: 123 };
      jest.spyOn(vaccinationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vaccination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vaccinationService.update).toHaveBeenCalledWith(vaccination);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTypeVaccinById', () => {
      it('Should return tracked TypeVaccin primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypeVaccinById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAnnonceById', () => {
      it('Should return tracked Annonce primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAnnonceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
