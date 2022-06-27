import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EquipeService } from '../service/equipe.service';
import { IEquipe, Equipe } from '../equipe.model';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { EquipeUpdateComponent } from './equipe-update.component';

describe('Equipe Management Update Component', () => {
  let comp: EquipeUpdateComponent;
  let fixture: ComponentFixture<EquipeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let equipeService: EquipeService;
  let typeSportService: TypeSportService;
  let quartierService: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EquipeUpdateComponent],
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
      .overrideTemplate(EquipeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EquipeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    equipeService = TestBed.inject(EquipeService);
    typeSportService = TestBed.inject(TypeSportService);
    quartierService = TestBed.inject(QuartierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call typeSport query and add missing value', () => {
      const equipe: IEquipe = { id: 456 };
      const typeSport: ITypeSport = { id: 46305 };
      equipe.typeSport = typeSport;

      const typeSportCollection: ITypeSport[] = [{ id: 54828 }];
      jest.spyOn(typeSportService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSportCollection })));
      const expectedCollection: ITypeSport[] = [typeSport, ...typeSportCollection];
      jest.spyOn(typeSportService, 'addTypeSportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      expect(typeSportService.query).toHaveBeenCalled();
      expect(typeSportService.addTypeSportToCollectionIfMissing).toHaveBeenCalledWith(typeSportCollection, typeSport);
      expect(comp.typeSportsCollection).toEqual(expectedCollection);
    });

    it('Should call Quartier query and add missing value', () => {
      const equipe: IEquipe = { id: 456 };
      const quartier: IQuartier = { id: 35967 };
      equipe.quartier = quartier;

      const quartierCollection: IQuartier[] = [{ id: 22942 }];
      jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
      const additionalQuartiers = [quartier];
      const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
      jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      expect(quartierService.query).toHaveBeenCalled();
      expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
      expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const equipe: IEquipe = { id: 456 };
      const typeSport: ITypeSport = { id: 76416 };
      equipe.typeSport = typeSport;
      const quartier: IQuartier = { id: 18696 };
      equipe.quartier = quartier;

      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(equipe));
      expect(comp.typeSportsCollection).toContain(typeSport);
      expect(comp.quartiersSharedCollection).toContain(quartier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Equipe>>();
      const equipe = { id: 123 };
      jest.spyOn(equipeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipe }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(equipeService.update).toHaveBeenCalledWith(equipe);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Equipe>>();
      const equipe = new Equipe();
      jest.spyOn(equipeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equipe }));
      saveSubject.complete();

      // THEN
      expect(equipeService.create).toHaveBeenCalledWith(equipe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Equipe>>();
      const equipe = { id: 123 };
      jest.spyOn(equipeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(equipeService.update).toHaveBeenCalledWith(equipe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTypeSportById', () => {
      it('Should return tracked TypeSport primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypeSportById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackQuartierById', () => {
      it('Should return tracked Quartier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuartierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
