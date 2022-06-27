import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeInterviewService } from '../service/demande-interview.service';
import { IDemandeInterview, DemandeInterview } from '../demande-interview.model';
import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';
import { EntrepreneurService } from 'app/entities/entrepreneur/service/entrepreneur.service';

import { DemandeInterviewUpdateComponent } from './demande-interview-update.component';

describe('DemandeInterview Management Update Component', () => {
  let comp: DemandeInterviewUpdateComponent;
  let fixture: ComponentFixture<DemandeInterviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeInterviewService: DemandeInterviewService;
  let entrepreneurService: EntrepreneurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeInterviewUpdateComponent],
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
      .overrideTemplate(DemandeInterviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeInterviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeInterviewService = TestBed.inject(DemandeInterviewService);
    entrepreneurService = TestBed.inject(EntrepreneurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call entrepreneur query and add missing value', () => {
      const demandeInterview: IDemandeInterview = { id: 456 };
      const entrepreneur: IEntrepreneur = { id: 5034 };
      demandeInterview.entrepreneur = entrepreneur;

      const entrepreneurCollection: IEntrepreneur[] = [{ id: 39660 }];
      jest.spyOn(entrepreneurService, 'query').mockReturnValue(of(new HttpResponse({ body: entrepreneurCollection })));
      const expectedCollection: IEntrepreneur[] = [entrepreneur, ...entrepreneurCollection];
      jest.spyOn(entrepreneurService, 'addEntrepreneurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeInterview });
      comp.ngOnInit();

      expect(entrepreneurService.query).toHaveBeenCalled();
      expect(entrepreneurService.addEntrepreneurToCollectionIfMissing).toHaveBeenCalledWith(entrepreneurCollection, entrepreneur);
      expect(comp.entrepreneursCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeInterview: IDemandeInterview = { id: 456 };
      const entrepreneur: IEntrepreneur = { id: 12608 };
      demandeInterview.entrepreneur = entrepreneur;

      activatedRoute.data = of({ demandeInterview });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeInterview));
      expect(comp.entrepreneursCollection).toContain(entrepreneur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInterview>>();
      const demandeInterview = { id: 123 };
      jest.spyOn(demandeInterviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInterview }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeInterviewService.update).toHaveBeenCalledWith(demandeInterview);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInterview>>();
      const demandeInterview = new DemandeInterview();
      jest.spyOn(demandeInterviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInterview }));
      saveSubject.complete();

      // THEN
      expect(demandeInterviewService.create).toHaveBeenCalledWith(demandeInterview);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInterview>>();
      const demandeInterview = { id: 123 };
      jest.spyOn(demandeInterviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeInterviewService.update).toHaveBeenCalledWith(demandeInterview);
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
  });
});
