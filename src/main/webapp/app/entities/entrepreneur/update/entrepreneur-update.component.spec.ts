import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EntrepreneurService } from '../service/entrepreneur.service';
import { IEntrepreneur, Entrepreneur } from '../entrepreneur.model';
import { IEntreprenariat } from 'app/entities/entreprenariat/entreprenariat.model';
import { EntreprenariatService } from 'app/entities/entreprenariat/service/entreprenariat.service';
import { IDomaineActivite } from 'app/entities/domaine-activite/domaine-activite.model';
import { DomaineActiviteService } from 'app/entities/domaine-activite/service/domaine-activite.service';

import { EntrepreneurUpdateComponent } from './entrepreneur-update.component';

describe('Entrepreneur Management Update Component', () => {
  let comp: EntrepreneurUpdateComponent;
  let fixture: ComponentFixture<EntrepreneurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let entrepreneurService: EntrepreneurService;
  let entreprenariatService: EntreprenariatService;
  let domaineActiviteService: DomaineActiviteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EntrepreneurUpdateComponent],
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
      .overrideTemplate(EntrepreneurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntrepreneurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entrepreneurService = TestBed.inject(EntrepreneurService);
    entreprenariatService = TestBed.inject(EntreprenariatService);
    domaineActiviteService = TestBed.inject(DomaineActiviteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call entreprenariat query and add missing value', () => {
      const entrepreneur: IEntrepreneur = { id: 456 };
      const entreprenariat: IEntreprenariat = { id: 3125 };
      entrepreneur.entreprenariat = entreprenariat;

      const entreprenariatCollection: IEntreprenariat[] = [{ id: 61214 }];
      jest.spyOn(entreprenariatService, 'query').mockReturnValue(of(new HttpResponse({ body: entreprenariatCollection })));
      const expectedCollection: IEntreprenariat[] = [entreprenariat, ...entreprenariatCollection];
      jest.spyOn(entreprenariatService, 'addEntreprenariatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      expect(entreprenariatService.query).toHaveBeenCalled();
      expect(entreprenariatService.addEntreprenariatToCollectionIfMissing).toHaveBeenCalledWith(entreprenariatCollection, entreprenariat);
      expect(comp.entreprenariatsCollection).toEqual(expectedCollection);
    });

    it('Should call domaineActivite query and add missing value', () => {
      const entrepreneur: IEntrepreneur = { id: 456 };
      const domaineActivite: IDomaineActivite = { id: 54597 };
      entrepreneur.domaineActivite = domaineActivite;

      const domaineActiviteCollection: IDomaineActivite[] = [{ id: 2043 }];
      jest.spyOn(domaineActiviteService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineActiviteCollection })));
      const expectedCollection: IDomaineActivite[] = [domaineActivite, ...domaineActiviteCollection];
      jest.spyOn(domaineActiviteService, 'addDomaineActiviteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      expect(domaineActiviteService.query).toHaveBeenCalled();
      expect(domaineActiviteService.addDomaineActiviteToCollectionIfMissing).toHaveBeenCalledWith(
        domaineActiviteCollection,
        domaineActivite
      );
      expect(comp.domaineActivitesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const entrepreneur: IEntrepreneur = { id: 456 };
      const entreprenariat: IEntreprenariat = { id: 84429 };
      entrepreneur.entreprenariat = entreprenariat;
      const domaineActivite: IDomaineActivite = { id: 35757 };
      entrepreneur.domaineActivite = domaineActivite;

      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(entrepreneur));
      expect(comp.entreprenariatsCollection).toContain(entreprenariat);
      expect(comp.domaineActivitesCollection).toContain(domaineActivite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entrepreneur>>();
      const entrepreneur = { id: 123 };
      jest.spyOn(entrepreneurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entrepreneur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(entrepreneurService.update).toHaveBeenCalledWith(entrepreneur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entrepreneur>>();
      const entrepreneur = new Entrepreneur();
      jest.spyOn(entrepreneurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entrepreneur }));
      saveSubject.complete();

      // THEN
      expect(entrepreneurService.create).toHaveBeenCalledWith(entrepreneur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entrepreneur>>();
      const entrepreneur = { id: 123 };
      jest.spyOn(entrepreneurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entrepreneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entrepreneurService.update).toHaveBeenCalledWith(entrepreneur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEntreprenariatById', () => {
      it('Should return tracked Entreprenariat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEntreprenariatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDomaineActiviteById', () => {
      it('Should return tracked DomaineActivite primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDomaineActiviteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
