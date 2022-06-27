import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartenairesService } from '../service/partenaires.service';
import { IPartenaires, Partenaires } from '../partenaires.model';
import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';
import { EntrepreneurService } from 'app/entities/entrepreneur/service/entrepreneur.service';
import { IEleveur } from 'app/entities/eleveur/eleveur.model';
import { EleveurService } from 'app/entities/eleveur/service/eleveur.service';

import { PartenairesUpdateComponent } from './partenaires-update.component';

describe('Partenaires Management Update Component', () => {
  let comp: PartenairesUpdateComponent;
  let fixture: ComponentFixture<PartenairesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partenairesService: PartenairesService;
  let entrepreneurService: EntrepreneurService;
  let eleveurService: EleveurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartenairesUpdateComponent],
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
      .overrideTemplate(PartenairesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartenairesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partenairesService = TestBed.inject(PartenairesService);
    entrepreneurService = TestBed.inject(EntrepreneurService);
    eleveurService = TestBed.inject(EleveurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call entrepreneur query and add missing value', () => {
      const partenaires: IPartenaires = { id: 456 };
      const entrepreneur: IEntrepreneur = { id: 33040 };
      partenaires.entrepreneur = entrepreneur;

      const entrepreneurCollection: IEntrepreneur[] = [{ id: 38390 }];
      jest.spyOn(entrepreneurService, 'query').mockReturnValue(of(new HttpResponse({ body: entrepreneurCollection })));
      const expectedCollection: IEntrepreneur[] = [entrepreneur, ...entrepreneurCollection];
      jest.spyOn(entrepreneurService, 'addEntrepreneurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      expect(entrepreneurService.query).toHaveBeenCalled();
      expect(entrepreneurService.addEntrepreneurToCollectionIfMissing).toHaveBeenCalledWith(entrepreneurCollection, entrepreneur);
      expect(comp.entrepreneursCollection).toEqual(expectedCollection);
    });

    it('Should call eleveur query and add missing value', () => {
      const partenaires: IPartenaires = { id: 456 };
      const eleveur: IEleveur = { id: 34663 };
      partenaires.eleveur = eleveur;

      const eleveurCollection: IEleveur[] = [{ id: 4689 }];
      jest.spyOn(eleveurService, 'query').mockReturnValue(of(new HttpResponse({ body: eleveurCollection })));
      const expectedCollection: IEleveur[] = [eleveur, ...eleveurCollection];
      jest.spyOn(eleveurService, 'addEleveurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      expect(eleveurService.query).toHaveBeenCalled();
      expect(eleveurService.addEleveurToCollectionIfMissing).toHaveBeenCalledWith(eleveurCollection, eleveur);
      expect(comp.eleveursCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const partenaires: IPartenaires = { id: 456 };
      const entrepreneur: IEntrepreneur = { id: 34667 };
      partenaires.entrepreneur = entrepreneur;
      const eleveur: IEleveur = { id: 50521 };
      partenaires.eleveur = eleveur;

      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(partenaires));
      expect(comp.entrepreneursCollection).toContain(entrepreneur);
      expect(comp.eleveursCollection).toContain(eleveur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Partenaires>>();
      const partenaires = { id: 123 };
      jest.spyOn(partenairesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partenaires }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(partenairesService.update).toHaveBeenCalledWith(partenaires);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Partenaires>>();
      const partenaires = new Partenaires();
      jest.spyOn(partenairesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partenaires }));
      saveSubject.complete();

      // THEN
      expect(partenairesService.create).toHaveBeenCalledWith(partenaires);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Partenaires>>();
      const partenaires = { id: 123 };
      jest.spyOn(partenairesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaires });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partenairesService.update).toHaveBeenCalledWith(partenaires);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEntrepreneurById', () => {
      it('Should return tracked Entrepreneur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEntrepreneurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEleveurById', () => {
      it('Should return tracked Eleveur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEleveurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
