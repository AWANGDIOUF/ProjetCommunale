import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';
import { ISensibiisationInternet, SensibiisationInternet } from '../sensibiisation-internet.model';

import { SensibiisationInternetUpdateComponent } from './sensibiisation-internet-update.component';

describe('SensibiisationInternet Management Update Component', () => {
  let comp: SensibiisationInternetUpdateComponent;
  let fixture: ComponentFixture<SensibiisationInternetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensibiisationInternetService: SensibiisationInternetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensibiisationInternetUpdateComponent],
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
      .overrideTemplate(SensibiisationInternetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensibiisationInternetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensibiisationInternetService = TestBed.inject(SensibiisationInternetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sensibiisationInternet: ISensibiisationInternet = { id: 456 };

      activatedRoute.data = of({ sensibiisationInternet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensibiisationInternet));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensibiisationInternet>>();
      const sensibiisationInternet = { id: 123 };
      jest.spyOn(sensibiisationInternetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensibiisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensibiisationInternet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensibiisationInternetService.update).toHaveBeenCalledWith(sensibiisationInternet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensibiisationInternet>>();
      const sensibiisationInternet = new SensibiisationInternet();
      jest.spyOn(sensibiisationInternetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensibiisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensibiisationInternet }));
      saveSubject.complete();

      // THEN
      expect(sensibiisationInternetService.create).toHaveBeenCalledWith(sensibiisationInternet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensibiisationInternet>>();
      const sensibiisationInternet = { id: 123 };
      jest.spyOn(sensibiisationInternetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensibiisationInternet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensibiisationInternetService.update).toHaveBeenCalledWith(sensibiisationInternet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
