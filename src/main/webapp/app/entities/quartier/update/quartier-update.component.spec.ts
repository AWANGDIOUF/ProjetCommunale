jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuartierService } from '../service/quartier.service';
import { IQuartier, Quartier } from '../quartier.model';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { BeneficiaireService } from 'app/entities/beneficiaire/service/beneficiaire.service';

import { QuartierUpdateComponent } from './quartier-update.component';

describe('Component Tests', () => {
  describe('Quartier Management Update Component', () => {
    let comp: QuartierUpdateComponent;
    let fixture: ComponentFixture<QuartierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quartierService: QuartierService;
    let equipeService: EquipeService;
    let clubService: ClubService;
    let beneficiaireService: BeneficiaireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuartierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuartierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuartierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quartierService = TestBed.inject(QuartierService);
      equipeService = TestBed.inject(EquipeService);
      clubService = TestBed.inject(ClubService);
      beneficiaireService = TestBed.inject(BeneficiaireService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Equipe query and add missing value', () => {
        const quartier: IQuartier = { id: 456 };
        const equipe: IEquipe = { id: 78985 };
        quartier.equipe = equipe;

        const equipeCollection: IEquipe[] = [{ id: 59492 }];
        jest.spyOn(equipeService, 'query').mockReturnValue(of(new HttpResponse({ body: equipeCollection })));
        const additionalEquipes = [equipe];
        const expectedCollection: IEquipe[] = [...additionalEquipes, ...equipeCollection];
        jest.spyOn(equipeService, 'addEquipeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(equipeService.query).toHaveBeenCalled();
        expect(equipeService.addEquipeToCollectionIfMissing).toHaveBeenCalledWith(equipeCollection, ...additionalEquipes);
        expect(comp.equipesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Club query and add missing value', () => {
        const quartier: IQuartier = { id: 456 };
        const club: IClub = { id: 11609 };
        quartier.club = club;

        const clubCollection: IClub[] = [{ id: 55911 }];
        jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
        const additionalClubs = [club];
        const expectedCollection: IClub[] = [...additionalClubs, ...clubCollection];
        jest.spyOn(clubService, 'addClubToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(clubService.query).toHaveBeenCalled();
        expect(clubService.addClubToCollectionIfMissing).toHaveBeenCalledWith(clubCollection, ...additionalClubs);
        expect(comp.clubsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Beneficiaire query and add missing value', () => {
        const quartier: IQuartier = { id: 456 };
        const beneficiaire: IBeneficiaire = { id: 15788 };
        quartier.beneficiaire = beneficiaire;

        const beneficiaireCollection: IBeneficiaire[] = [{ id: 20787 }];
        jest.spyOn(beneficiaireService, 'query').mockReturnValue(of(new HttpResponse({ body: beneficiaireCollection })));
        const additionalBeneficiaires = [beneficiaire];
        const expectedCollection: IBeneficiaire[] = [...additionalBeneficiaires, ...beneficiaireCollection];
        jest.spyOn(beneficiaireService, 'addBeneficiaireToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(beneficiaireService.query).toHaveBeenCalled();
        expect(beneficiaireService.addBeneficiaireToCollectionIfMissing).toHaveBeenCalledWith(
          beneficiaireCollection,
          ...additionalBeneficiaires
        );
        expect(comp.beneficiairesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const quartier: IQuartier = { id: 456 };
        const equipe: IEquipe = { id: 36487 };
        quartier.equipe = equipe;
        const club: IClub = { id: 39751 };
        quartier.club = club;
        const beneficiaire: IBeneficiaire = { id: 17819 };
        quartier.beneficiaire = beneficiaire;

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(quartier));
        expect(comp.equipesSharedCollection).toContain(equipe);
        expect(comp.clubsSharedCollection).toContain(club);
        expect(comp.beneficiairesSharedCollection).toContain(beneficiaire);
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
      describe('trackEquipeById', () => {
        it('Should return tracked Equipe primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEquipeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClubById', () => {
        it('Should return tracked Club primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClubById(0, entity);
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
});
