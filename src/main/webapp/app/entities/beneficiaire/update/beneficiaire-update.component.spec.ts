jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BeneficiaireService } from '../service/beneficiaire.service';
import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

import { BeneficiaireUpdateComponent } from './beneficiaire-update.component';

describe('Component Tests', () => {
  describe('Beneficiaire Management Update Component', () => {
    let comp: BeneficiaireUpdateComponent;
    let fixture: ComponentFixture<BeneficiaireUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let beneficiaireService: BeneficiaireService;
    let annonceService: AnnonceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BeneficiaireUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BeneficiaireUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BeneficiaireUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      beneficiaireService = TestBed.inject(BeneficiaireService);
      annonceService = TestBed.inject(AnnonceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Annonce query and add missing value', () => {
        const beneficiaire: IBeneficiaire = { id: 456 };
        const annonce: IAnnonce = { id: 36181 };
        beneficiaire.annonce = annonce;

        const annonceCollection: IAnnonce[] = [{ id: 35476 }];
        jest.spyOn(annonceService, 'query').mockReturnValue(of(new HttpResponse({ body: annonceCollection })));
        const additionalAnnonces = [annonce];
        const expectedCollection: IAnnonce[] = [...additionalAnnonces, ...annonceCollection];
        jest.spyOn(annonceService, 'addAnnonceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ beneficiaire });
        comp.ngOnInit();

        expect(annonceService.query).toHaveBeenCalled();
        expect(annonceService.addAnnonceToCollectionIfMissing).toHaveBeenCalledWith(annonceCollection, ...additionalAnnonces);
        expect(comp.annoncesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const beneficiaire: IBeneficiaire = { id: 456 };
        const annonce: IAnnonce = { id: 48736 };
        beneficiaire.annonce = annonce;

        activatedRoute.data = of({ beneficiaire });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(beneficiaire));
        expect(comp.annoncesSharedCollection).toContain(annonce);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
        const beneficiaire = { id: 123 };
        jest.spyOn(beneficiaireService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ beneficiaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: beneficiaire }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(beneficiaireService.update).toHaveBeenCalledWith(beneficiaire);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
        const beneficiaire = new Beneficiaire();
        jest.spyOn(beneficiaireService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ beneficiaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: beneficiaire }));
        saveSubject.complete();

        // THEN
        expect(beneficiaireService.create).toHaveBeenCalledWith(beneficiaire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
        const beneficiaire = { id: 123 };
        jest.spyOn(beneficiaireService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ beneficiaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(beneficiaireService.update).toHaveBeenCalledWith(beneficiaire);
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
