import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DomaineActiviteService } from '../service/domaine-activite.service';
import { IDomaineActivite, DomaineActivite } from '../domaine-activite.model';

import { DomaineActiviteUpdateComponent } from './domaine-activite-update.component';

describe('DomaineActivite Management Update Component', () => {
  let comp: DomaineActiviteUpdateComponent;
  let fixture: ComponentFixture<DomaineActiviteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domaineActiviteService: DomaineActiviteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DomaineActiviteUpdateComponent],
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
      .overrideTemplate(DomaineActiviteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomaineActiviteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domaineActiviteService = TestBed.inject(DomaineActiviteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const domaineActivite: IDomaineActivite = { id: 456 };

      activatedRoute.data = of({ domaineActivite });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(domaineActivite));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DomaineActivite>>();
      const domaineActivite = { id: 123 };
      jest.spyOn(domaineActiviteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineActivite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaineActivite }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(domaineActiviteService.update).toHaveBeenCalledWith(domaineActivite);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DomaineActivite>>();
      const domaineActivite = new DomaineActivite();
      jest.spyOn(domaineActiviteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineActivite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaineActivite }));
      saveSubject.complete();

      // THEN
      expect(domaineActiviteService.create).toHaveBeenCalledWith(domaineActivite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DomaineActivite>>();
      const domaineActivite = { id: 123 };
      jest.spyOn(domaineActiviteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineActivite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domaineActiviteService.update).toHaveBeenCalledWith(domaineActivite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
