import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UtilisationInternetService } from '../service/utilisation-internet.service';
import { IUtilisationInternet, UtilisationInternet } from '../utilisation-internet.model';
import { ILogiciel } from 'app/entities/logiciel/logiciel.model';
import { LogicielService } from 'app/entities/logiciel/service/logiciel.service';

import { UtilisationInternetUpdateComponent } from './utilisation-internet-update.component';

describe('UtilisationInternet Management Update Component', () => {
  let comp: UtilisationInternetUpdateComponent;
  let fixture: ComponentFixture<UtilisationInternetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilisationInternetService: UtilisationInternetService;
  let logicielService: LogicielService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UtilisationInternetUpdateComponent],
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
      .overrideTemplate(UtilisationInternetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilisationInternetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisationInternetService = TestBed.inject(UtilisationInternetService);
    logicielService = TestBed.inject(LogicielService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call logiciel query and add missing value', () => {
      const utilisationInternet: IUtilisationInternet = { id: 456 };
      const logiciel: ILogiciel = { id: 58370 };
      utilisationInternet.logiciel = logiciel;

      const logicielCollection: ILogiciel[] = [{ id: 1827 }];
      jest.spyOn(logicielService, 'query').mockReturnValue(of(new HttpResponse({ body: logicielCollection })));
      const expectedCollection: ILogiciel[] = [logiciel, ...logicielCollection];
      jest.spyOn(logicielService, 'addLogicielToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisationInternet });
      comp.ngOnInit();

      expect(logicielService.query).toHaveBeenCalled();
      expect(logicielService.addLogicielToCollectionIfMissing).toHaveBeenCalledWith(logicielCollection, logiciel);
      expect(comp.logicielsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const utilisationInternet: IUtilisationInternet = { id: 456 };
      const logiciel: ILogiciel = { id: 6703 };
      utilisationInternet.logiciel = logiciel;

      activatedRoute.data = of({ utilisationInternet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(utilisationInternet));
      expect(comp.logicielsCollection).toContain(logiciel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UtilisationInternet>>();
      const utilisationInternet = { id: 123 };
      jest.spyOn(utilisationInternetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisationInternet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisationInternetService.update).toHaveBeenCalledWith(utilisationInternet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UtilisationInternet>>();
      const utilisationInternet = new UtilisationInternet();
      jest.spyOn(utilisationInternetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisationInternet }));
      saveSubject.complete();

      // THEN
      expect(utilisationInternetService.create).toHaveBeenCalledWith(utilisationInternet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UtilisationInternet>>();
      const utilisationInternet = { id: 123 };
      jest.spyOn(utilisationInternetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisationInternetService.update).toHaveBeenCalledWith(utilisationInternet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLogicielById', () => {
      it('Should return tracked Logiciel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLogicielById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
