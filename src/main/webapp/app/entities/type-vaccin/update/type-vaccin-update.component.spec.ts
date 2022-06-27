import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeVaccinService } from '../service/type-vaccin.service';
import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';

import { TypeVaccinUpdateComponent } from './type-vaccin-update.component';

describe('TypeVaccin Management Update Component', () => {
  let comp: TypeVaccinUpdateComponent;
  let fixture: ComponentFixture<TypeVaccinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeVaccinService: TypeVaccinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeVaccinUpdateComponent],
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
      .overrideTemplate(TypeVaccinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeVaccinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeVaccinService = TestBed.inject(TypeVaccinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeVaccin: ITypeVaccin = { id: 456 };

      activatedRoute.data = of({ typeVaccin });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typeVaccin));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
      const typeVaccin = { id: 123 };
      jest.spyOn(typeVaccinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeVaccin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeVaccin }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeVaccinService.update).toHaveBeenCalledWith(typeVaccin);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
      const typeVaccin = new TypeVaccin();
      jest.spyOn(typeVaccinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeVaccin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeVaccin }));
      saveSubject.complete();

      // THEN
      expect(typeVaccinService.create).toHaveBeenCalledWith(typeVaccin);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeVaccin>>();
      const typeVaccin = { id: 123 };
      jest.spyOn(typeVaccinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeVaccin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeVaccinService.update).toHaveBeenCalledWith(typeVaccin);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
