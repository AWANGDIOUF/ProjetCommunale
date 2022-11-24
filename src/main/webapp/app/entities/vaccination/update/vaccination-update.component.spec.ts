jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VaccinationService } from '../service/vaccination.service';
import { IVaccination, Vaccination } from '../vaccination.model';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

import { VaccinationUpdateComponent } from './vaccination-update.component';

describe('Component Tests', () => {
  describe('Vaccination Management Update Component', () => {
    let comp: VaccinationUpdateComponent;
    let fixture: ComponentFixture<VaccinationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vaccinationService: VaccinationService;
    let annonceService: AnnonceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VaccinationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VaccinationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VaccinationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vaccinationService = TestBed.inject(VaccinationService);
      annonceService = TestBed.inject(AnnonceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
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
        const annonce: IAnnonce = { id: 29686 };
        vaccination.annonce = annonce;

        activatedRoute.data = of({ vaccination });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vaccination));
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
      describe('trackAnnonceById', () => {
        it('Should return tracked Annonce primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAnnonceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
