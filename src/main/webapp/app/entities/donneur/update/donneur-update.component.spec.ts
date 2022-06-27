import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DonneurService } from '../service/donneur.service';
import { IDonneur, Donneur } from '../donneur.model';

import { DonneurUpdateComponent } from './donneur-update.component';

describe('Donneur Management Update Component', () => {
  let comp: DonneurUpdateComponent;
  let fixture: ComponentFixture<DonneurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donneurService: DonneurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DonneurUpdateComponent],
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
      .overrideTemplate(DonneurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonneurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donneurService = TestBed.inject(DonneurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const donneur: IDonneur = { id: 456 };

      activatedRoute.data = of({ donneur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(donneur));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donneur>>();
      const donneur = { id: 123 };
      jest.spyOn(donneurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donneur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(donneurService.update).toHaveBeenCalledWith(donneur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donneur>>();
      const donneur = new Donneur();
      jest.spyOn(donneurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donneur }));
      saveSubject.complete();

      // THEN
      expect(donneurService.create).toHaveBeenCalledWith(donneur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donneur>>();
      const donneur = { id: 123 };
      jest.spyOn(donneurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donneur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donneurService.update).toHaveBeenCalledWith(donneur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
