import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PropositionService } from '../service/proposition.service';
import { IProposition, Proposition } from '../proposition.model';
import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';
import { EnsegnantService } from 'app/entities/ensegnant/service/ensegnant.service';

import { PropositionUpdateComponent } from './proposition-update.component';

describe('Proposition Management Update Component', () => {
  let comp: PropositionUpdateComponent;
  let fixture: ComponentFixture<PropositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let propositionService: PropositionService;
  let ensegnantService: EnsegnantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PropositionUpdateComponent],
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
      .overrideTemplate(PropositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    propositionService = TestBed.inject(PropositionService);
    ensegnantService = TestBed.inject(EnsegnantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ensegnant query and add missing value', () => {
      const proposition: IProposition = { id: 456 };
      const enseignant: IEnsegnant = { id: 12822 };
      proposition.enseignant = enseignant;

      const ensegnantCollection: IEnsegnant[] = [{ id: 82964 }];
      jest.spyOn(ensegnantService, 'query').mockReturnValue(of(new HttpResponse({ body: ensegnantCollection })));
      const additionalEnsegnants = [enseignant];
      const expectedCollection: IEnsegnant[] = [...additionalEnsegnants, ...ensegnantCollection];
      jest.spyOn(ensegnantService, 'addEnsegnantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proposition });
      comp.ngOnInit();

      expect(ensegnantService.query).toHaveBeenCalled();
      expect(ensegnantService.addEnsegnantToCollectionIfMissing).toHaveBeenCalledWith(ensegnantCollection, ...additionalEnsegnants);
      expect(comp.ensegnantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const proposition: IProposition = { id: 456 };
      const enseignant: IEnsegnant = { id: 76671 };
      proposition.enseignant = enseignant;

      activatedRoute.data = of({ proposition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(proposition));
      expect(comp.ensegnantsSharedCollection).toContain(enseignant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Proposition>>();
      const proposition = { id: 123 };
      jest.spyOn(propositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(propositionService.update).toHaveBeenCalledWith(proposition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Proposition>>();
      const proposition = new Proposition();
      jest.spyOn(propositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposition }));
      saveSubject.complete();

      // THEN
      expect(propositionService.create).toHaveBeenCalledWith(proposition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Proposition>>();
      const proposition = { id: 123 };
      jest.spyOn(propositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(propositionService.update).toHaveBeenCalledWith(proposition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEnsegnantById', () => {
      it('Should return tracked Ensegnant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnsegnantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
