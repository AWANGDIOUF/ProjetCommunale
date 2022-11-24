jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeVaccinService } from '../service/type-vaccin.service';
import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';
import { IVaccination } from 'app/entities/vaccination/vaccination.model';
import { VaccinationService } from 'app/entities/vaccination/service/vaccination.service';

import { TypeVaccinUpdateComponent } from './type-vaccin-update.component';

describe('Component Tests', () => {
  describe('TypeVaccin Management Update Component', () => {
    let comp: TypeVaccinUpdateComponent;
    let fixture: ComponentFixture<TypeVaccinUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeVaccinService: TypeVaccinService;
    let vaccinationService: VaccinationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeVaccinUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeVaccinUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeVaccinUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeVaccinService = TestBed.inject(TypeVaccinService);
      vaccinationService = TestBed.inject(VaccinationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call vaccination query and add missing value', () => {
        const typeVaccin: ITypeVaccin = { id: 456 };
        const vaccination: IVaccination = { id: 14283 };
        typeVaccin.vaccination = vaccination;

        const vaccinationCollection: IVaccination[] = [{ id: 92475 }];
        jest.spyOn(vaccinationService, 'query').mockReturnValue(of(new HttpResponse({ body: vaccinationCollection })));
        const expectedCollection: IVaccination[] = [vaccination, ...vaccinationCollection];
        jest.spyOn(vaccinationService, 'addVaccinationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ typeVaccin });
        comp.ngOnInit();

        expect(vaccinationService.query).toHaveBeenCalled();
        expect(vaccinationService.addVaccinationToCollectionIfMissing).toHaveBeenCalledWith(vaccinationCollection, vaccination);
        expect(comp.vaccinationsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const typeVaccin: ITypeVaccin = { id: 456 };
        const vaccination: IVaccination = { id: 89660 };
        typeVaccin.vaccination = vaccination;

        activatedRoute.data = of({ typeVaccin });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(typeVaccin));
        expect(comp.vaccinationsCollection).toContain(vaccination);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
        const typeVaccin = { id: 123 };
        jest.spyOn(typeVaccinService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeVaccin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeVaccin }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeVaccinService.update).toHaveBeenCalledWith(typeVaccin);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
        const typeVaccin = new TypeVaccin();
        jest.spyOn(typeVaccinService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeVaccin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeVaccin }));
        saveSubject.complete();

        // THEN
        expect(typeVaccinService.create).toHaveBeenCalledWith(typeVaccin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
        const typeVaccin = { id: 123 };
        jest.spyOn(typeVaccinService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeVaccin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeVaccinService.update).toHaveBeenCalledWith(typeVaccin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVaccinationById', () => {
        it('Should return tracked Vaccination primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVaccinationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
