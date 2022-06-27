import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeSportService } from '../service/type-sport.service';
import { ITypeSport, TypeSport } from '../type-sport.model';

import { TypeSportUpdateComponent } from './type-sport-update.component';

describe('TypeSport Management Update Component', () => {
  let comp: TypeSportUpdateComponent;
  let fixture: ComponentFixture<TypeSportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeSportService: TypeSportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeSportUpdateComponent],
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
      .overrideTemplate(TypeSportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeSportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeSportService = TestBed.inject(TypeSportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeSport: ITypeSport = { id: 456 };

      activatedRoute.data = of({ typeSport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typeSport));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeSport>>();
      const typeSport = { id: 123 };
      jest.spyOn(typeSportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeSportService.update).toHaveBeenCalledWith(typeSport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeSport>>();
      const typeSport = new TypeSport();
      jest.spyOn(typeSportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSport }));
      saveSubject.complete();

      // THEN
      expect(typeSportService.create).toHaveBeenCalledWith(typeSport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeSport>>();
      const typeSport = { id: 123 };
      jest.spyOn(typeSportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeSportService.update).toHaveBeenCalledWith(typeSport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
