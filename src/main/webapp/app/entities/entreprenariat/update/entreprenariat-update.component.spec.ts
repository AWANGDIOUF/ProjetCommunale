import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EntreprenariatService } from '../service/entreprenariat.service';
import { IEntreprenariat, Entreprenariat } from '../entreprenariat.model';

import { EntreprenariatUpdateComponent } from './entreprenariat-update.component';

describe('Entreprenariat Management Update Component', () => {
  let comp: EntreprenariatUpdateComponent;
  let fixture: ComponentFixture<EntreprenariatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let entreprenariatService: EntreprenariatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EntreprenariatUpdateComponent],
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
      .overrideTemplate(EntreprenariatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntreprenariatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entreprenariatService = TestBed.inject(EntreprenariatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const entreprenariat: IEntreprenariat = { id: 456 };

      activatedRoute.data = of({ entreprenariat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(entreprenariat));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprenariat>>();
      const entreprenariat = { id: 123 };
      jest.spyOn(entreprenariatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprenariat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entreprenariat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(entreprenariatService.update).toHaveBeenCalledWith(entreprenariat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprenariat>>();
      const entreprenariat = new Entreprenariat();
      jest.spyOn(entreprenariatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprenariat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: entreprenariat }));
      saveSubject.complete();

      // THEN
      expect(entreprenariatService.create).toHaveBeenCalledWith(entreprenariat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Entreprenariat>>();
      const entreprenariat = { id: 123 };
      jest.spyOn(entreprenariatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entreprenariat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entreprenariatService.update).toHaveBeenCalledWith(entreprenariat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
