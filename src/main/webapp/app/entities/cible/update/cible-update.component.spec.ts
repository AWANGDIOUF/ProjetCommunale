jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CibleService } from '../service/cible.service';
import { ICible, Cible } from '../cible.model';
import { IVaccination } from 'app/entities/vaccination/vaccination.model';
import { VaccinationService } from 'app/entities/vaccination/service/vaccination.service';

import { CibleUpdateComponent } from './cible-update.component';

describe('Component Tests', () => {
  describe('Cible Management Update Component', () => {
    let comp: CibleUpdateComponent;
    let fixture: ComponentFixture<CibleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cibleService: CibleService;
    let vaccinationService: VaccinationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CibleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CibleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CibleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cibleService = TestBed.inject(CibleService);
      vaccinationService = TestBed.inject(VaccinationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vaccination query and add missing value', () => {
        const cible: ICible = { id: 456 };
        const vaccination: IVaccination = { id: 51901 };
        cible.vaccination = vaccination;

        const vaccinationCollection: IVaccination[] = [{ id: 20316 }];
        jest.spyOn(vaccinationService, 'query').mockReturnValue(of(new HttpResponse({ body: vaccinationCollection })));
        const additionalVaccinations = [vaccination];
        const expectedCollection: IVaccination[] = [...additionalVaccinations, ...vaccinationCollection];
        jest.spyOn(vaccinationService, 'addVaccinationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cible });
        comp.ngOnInit();

        expect(vaccinationService.query).toHaveBeenCalled();
        expect(vaccinationService.addVaccinationToCollectionIfMissing).toHaveBeenCalledWith(
          vaccinationCollection,
          ...additionalVaccinations
        );
        expect(comp.vaccinationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cible: ICible = { id: 456 };
        const vaccination: IVaccination = { id: 82191 };
        cible.vaccination = vaccination;

        activatedRoute.data = of({ cible });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cible));
        expect(comp.vaccinationsSharedCollection).toContain(vaccination);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cible>>();
        const cible = { id: 123 };
        jest.spyOn(cibleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cible });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cible }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cibleService.update).toHaveBeenCalledWith(cible);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cible>>();
        const cible = new Cible();
        jest.spyOn(cibleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cible });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cible }));
        saveSubject.complete();

        // THEN
        expect(cibleService.create).toHaveBeenCalledWith(cible);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cible>>();
        const cible = { id: 123 };
        jest.spyOn(cibleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cible });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cibleService.update).toHaveBeenCalledWith(cible);
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
