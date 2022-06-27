import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EleveurService } from '../service/eleveur.service';
import { IEleveur, Eleveur } from '../eleveur.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { EleveurUpdateComponent } from './eleveur-update.component';

describe('Eleveur Management Update Component', () => {
  let comp: EleveurUpdateComponent;
  let fixture: ComponentFixture<EleveurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eleveurService: EleveurService;
  let quartierService: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EleveurUpdateComponent],
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
      .overrideTemplate(EleveurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EleveurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eleveurService = TestBed.inject(EleveurService);
    quartierService = TestBed.inject(QuartierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quartier query and add missing value', () => {
      const eleveur: IEleveur = { id: 456 };
      const quartier: IQuartier = { id: 86892 };
      eleveur.quartier = quartier;

      const quartierCollection: IQuartier[] = [{ id: 26122 }];
      jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
      const additionalQuartiers = [quartier];
      const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
      jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eleveur });
      comp.ngOnInit();

      expect(quartierService.query).toHaveBeenCalled();
      expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
      expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eleveur: IEleveur = { id: 456 };
      const quartier: IQuartier = { id: 10062 };
      eleveur.quartier = quartier;

      activatedRoute.data = of({ eleveur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(eleveur));
      expect(comp.quartiersSharedCollection).toContain(quartier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Eleveur>>();
      const eleveur = { id: 123 };
      jest.spyOn(eleveurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eleveur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eleveur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eleveurService.update).toHaveBeenCalledWith(eleveur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Eleveur>>();
      const eleveur = new Eleveur();
      jest.spyOn(eleveurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eleveur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eleveur }));
      saveSubject.complete();

      // THEN
      expect(eleveurService.create).toHaveBeenCalledWith(eleveur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Eleveur>>();
      const eleveur = { id: 123 };
      jest.spyOn(eleveurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eleveur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eleveurService.update).toHaveBeenCalledWith(eleveur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuartierById', () => {
      it('Should return tracked Quartier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuartierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
