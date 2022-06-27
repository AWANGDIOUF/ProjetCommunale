import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InscriptionService } from '../service/inscription.service';
import { IInscription, Inscription } from '../inscription.model';
import { IEvenement } from 'app/entities/evenement/evenement.model';
import { EvenementService } from 'app/entities/evenement/service/evenement.service';

import { InscriptionUpdateComponent } from './inscription-update.component';

describe('Inscription Management Update Component', () => {
  let comp: InscriptionUpdateComponent;
  let fixture: ComponentFixture<InscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionService: InscriptionService;
  let evenementService: EvenementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InscriptionUpdateComponent],
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
      .overrideTemplate(InscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionService = TestBed.inject(InscriptionService);
    evenementService = TestBed.inject(EvenementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call evenement query and add missing value', () => {
      const inscription: IInscription = { id: 456 };
      const evenement: IEvenement = { id: 65881 };
      inscription.evenement = evenement;

      const evenementCollection: IEvenement[] = [{ id: 56837 }];
      jest.spyOn(evenementService, 'query').mockReturnValue(of(new HttpResponse({ body: evenementCollection })));
      const expectedCollection: IEvenement[] = [evenement, ...evenementCollection];
      jest.spyOn(evenementService, 'addEvenementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(evenementService.query).toHaveBeenCalled();
      expect(evenementService.addEvenementToCollectionIfMissing).toHaveBeenCalledWith(evenementCollection, evenement);
      expect(comp.evenementsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscription: IInscription = { id: 456 };
      const evenement: IEvenement = { id: 4654 };
      inscription.evenement = evenement;

      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inscription));
      expect(comp.evenementsCollection).toContain(evenement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inscription>>();
      const inscription = { id: 123 };
      jest.spyOn(inscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscription }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionService.update).toHaveBeenCalledWith(inscription);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inscription>>();
      const inscription = new Inscription();
      jest.spyOn(inscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscription }));
      saveSubject.complete();

      // THEN
      expect(inscriptionService.create).toHaveBeenCalledWith(inscription);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inscription>>();
      const inscription = { id: 123 };
      jest.spyOn(inscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionService.update).toHaveBeenCalledWith(inscription);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEvenementById', () => {
      it('Should return tracked Evenement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEvenementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
