import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CalendrierEvenementService } from '../service/calendrier-evenement.service';
import { ICalendrierEvenement, CalendrierEvenement } from '../calendrier-evenement.model';

import { CalendrierEvenementUpdateComponent } from './calendrier-evenement-update.component';

describe('CalendrierEvenement Management Update Component', () => {
  let comp: CalendrierEvenementUpdateComponent;
  let fixture: ComponentFixture<CalendrierEvenementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let calendrierEvenementService: CalendrierEvenementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CalendrierEvenementUpdateComponent],
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
      .overrideTemplate(CalendrierEvenementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CalendrierEvenementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    calendrierEvenementService = TestBed.inject(CalendrierEvenementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const calendrierEvenement: ICalendrierEvenement = { id: 456 };

      activatedRoute.data = of({ calendrierEvenement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(calendrierEvenement));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CalendrierEvenement>>();
      const calendrierEvenement = { id: 123 };
      jest.spyOn(calendrierEvenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calendrierEvenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calendrierEvenement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(calendrierEvenementService.update).toHaveBeenCalledWith(calendrierEvenement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CalendrierEvenement>>();
      const calendrierEvenement = new CalendrierEvenement();
      jest.spyOn(calendrierEvenementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calendrierEvenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calendrierEvenement }));
      saveSubject.complete();

      // THEN
      expect(calendrierEvenementService.create).toHaveBeenCalledWith(calendrierEvenement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CalendrierEvenement>>();
      const calendrierEvenement = { id: 123 };
      jest.spyOn(calendrierEvenementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calendrierEvenement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(calendrierEvenementService.update).toHaveBeenCalledWith(calendrierEvenement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
