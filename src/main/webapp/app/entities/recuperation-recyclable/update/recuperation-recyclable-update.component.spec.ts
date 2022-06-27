import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';
import { IRecuperationRecyclable, RecuperationRecyclable } from '../recuperation-recyclable.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { RecuperationRecyclableUpdateComponent } from './recuperation-recyclable-update.component';

describe('RecuperationRecyclable Management Update Component', () => {
  let comp: RecuperationRecyclableUpdateComponent;
  let fixture: ComponentFixture<RecuperationRecyclableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recuperationRecyclableService: RecuperationRecyclableService;
  let quartierService: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecuperationRecyclableUpdateComponent],
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
      .overrideTemplate(RecuperationRecyclableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecuperationRecyclableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recuperationRecyclableService = TestBed.inject(RecuperationRecyclableService);
    quartierService = TestBed.inject(QuartierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quartier query and add missing value', () => {
      const recuperationRecyclable: IRecuperationRecyclable = { id: 456 };
      const quartier: IQuartier = { id: 2355 };
      recuperationRecyclable.quartier = quartier;

      const quartierCollection: IQuartier[] = [{ id: 95463 }];
      jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
      const additionalQuartiers = [quartier];
      const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
      jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recuperationRecyclable });
      comp.ngOnInit();

      expect(quartierService.query).toHaveBeenCalled();
      expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
      expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recuperationRecyclable: IRecuperationRecyclable = { id: 456 };
      const quartier: IQuartier = { id: 73040 };
      recuperationRecyclable.quartier = quartier;

      activatedRoute.data = of({ recuperationRecyclable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(recuperationRecyclable));
      expect(comp.quartiersSharedCollection).toContain(quartier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecuperationRecyclable>>();
      const recuperationRecyclable = { id: 123 };
      jest.spyOn(recuperationRecyclableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperationRecyclable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recuperationRecyclable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(recuperationRecyclableService.update).toHaveBeenCalledWith(recuperationRecyclable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecuperationRecyclable>>();
      const recuperationRecyclable = new RecuperationRecyclable();
      jest.spyOn(recuperationRecyclableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperationRecyclable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recuperationRecyclable }));
      saveSubject.complete();

      // THEN
      expect(recuperationRecyclableService.create).toHaveBeenCalledWith(recuperationRecyclable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecuperationRecyclable>>();
      const recuperationRecyclable = { id: 123 };
      jest.spyOn(recuperationRecyclableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperationRecyclable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recuperationRecyclableService.update).toHaveBeenCalledWith(recuperationRecyclable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackQuartierById', () => {
      it('Should return tracked Quartier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuartierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
