import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActivitePolitiqueService } from '../service/activite-politique.service';
import { IActivitePolitique, ActivitePolitique } from '../activite-politique.model';

import { ActivitePolitiqueUpdateComponent } from './activite-politique-update.component';

describe('ActivitePolitique Management Update Component', () => {
  let comp: ActivitePolitiqueUpdateComponent;
  let fixture: ComponentFixture<ActivitePolitiqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activitePolitiqueService: ActivitePolitiqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActivitePolitiqueUpdateComponent],
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
      .overrideTemplate(ActivitePolitiqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivitePolitiqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activitePolitiqueService = TestBed.inject(ActivitePolitiqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const activitePolitique: IActivitePolitique = { id: 456 };

      activatedRoute.data = of({ activitePolitique });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(activitePolitique));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivitePolitique>>();
      const activitePolitique = { id: 123 };
      jest.spyOn(activitePolitiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activitePolitique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activitePolitique }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(activitePolitiqueService.update).toHaveBeenCalledWith(activitePolitique);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivitePolitique>>();
      const activitePolitique = new ActivitePolitique();
      jest.spyOn(activitePolitiqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activitePolitique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activitePolitique }));
      saveSubject.complete();

      // THEN
      expect(activitePolitiqueService.create).toHaveBeenCalledWith(activitePolitique);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivitePolitique>>();
      const activitePolitique = { id: 123 };
      jest.spyOn(activitePolitiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activitePolitique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activitePolitiqueService.update).toHaveBeenCalledWith(activitePolitique);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
