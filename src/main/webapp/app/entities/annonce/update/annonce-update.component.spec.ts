import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AnnonceService } from '../service/annonce.service';
import { IAnnonce, Annonce } from '../annonce.model';
import { IDon } from 'app/entities/don/don.model';
import { DonService } from 'app/entities/don/service/don.service';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { BeneficiaireService } from 'app/entities/beneficiaire/service/beneficiaire.service';

import { AnnonceUpdateComponent } from './annonce-update.component';

describe('Annonce Management Update Component', () => {
  let comp: AnnonceUpdateComponent;
  let fixture: ComponentFixture<AnnonceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let annonceService: AnnonceService;
  let donService: DonService;
  let beneficiaireService: BeneficiaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnnonceUpdateComponent],
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
      .overrideTemplate(AnnonceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnnonceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    annonceService = TestBed.inject(AnnonceService);
    donService = TestBed.inject(DonService);
    beneficiaireService = TestBed.inject(BeneficiaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Don query and add missing value', () => {
      const annonce: IAnnonce = { id: 456 };
      const don: IDon = { id: 78254 };
      annonce.don = don;

      const donCollection: IDon[] = [{ id: 71765 }];
      jest.spyOn(donService, 'query').mockReturnValue(of(new HttpResponse({ body: donCollection })));
      const additionalDons = [don];
      const expectedCollection: IDon[] = [...additionalDons, ...donCollection];
      jest.spyOn(donService, 'addDonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(donService.query).toHaveBeenCalled();
      expect(donService.addDonToCollectionIfMissing).toHaveBeenCalledWith(donCollection, ...additionalDons);
      expect(comp.donsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Beneficiaire query and add missing value', () => {
      const annonce: IAnnonce = { id: 456 };
      const beneficiaire: IBeneficiaire = { id: 66796 };
      annonce.beneficiaire = beneficiaire;

      const beneficiaireCollection: IBeneficiaire[] = [{ id: 50646 }];
      jest.spyOn(beneficiaireService, 'query').mockReturnValue(of(new HttpResponse({ body: beneficiaireCollection })));
      const additionalBeneficiaires = [beneficiaire];
      const expectedCollection: IBeneficiaire[] = [...additionalBeneficiaires, ...beneficiaireCollection];
      jest.spyOn(beneficiaireService, 'addBeneficiaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(beneficiaireService.query).toHaveBeenCalled();
      expect(beneficiaireService.addBeneficiaireToCollectionIfMissing).toHaveBeenCalledWith(
        beneficiaireCollection,
        ...additionalBeneficiaires
      );
      expect(comp.beneficiairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const annonce: IAnnonce = { id: 456 };
      const don: IDon = { id: 22284 };
      annonce.don = don;
      const beneficiaire: IBeneficiaire = { id: 99223 };
      annonce.beneficiaire = beneficiaire;

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(annonce));
      expect(comp.donsSharedCollection).toContain(don);
      expect(comp.beneficiairesSharedCollection).toContain(beneficiaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = { id: 123 };
      jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annonce }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(annonceService.update).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = new Annonce();
      jest.spyOn(annonceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annonce }));
      saveSubject.complete();

      // THEN
      expect(annonceService.create).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = { id: 123 };
      jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(annonceService.update).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDonById', () => {
      it('Should return tracked Don primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBeneficiaireById', () => {
      it('Should return tracked Beneficiaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBeneficiaireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
