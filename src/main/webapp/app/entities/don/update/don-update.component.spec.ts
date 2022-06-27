import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DonService } from '../service/don.service';
import { IDon, Don } from '../don.model';
import { IDonneur } from 'app/entities/donneur/donneur.model';
import { DonneurService } from 'app/entities/donneur/service/donneur.service';

import { DonUpdateComponent } from './don-update.component';

describe('Don Management Update Component', () => {
  let comp: DonUpdateComponent;
  let fixture: ComponentFixture<DonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donService: DonService;
  let donneurService: DonneurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DonUpdateComponent],
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
      .overrideTemplate(DonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donService = TestBed.inject(DonService);
    donneurService = TestBed.inject(DonneurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Donneur query and add missing value', () => {
      const don: IDon = { id: 456 };
      const donneur: IDonneur = { id: 89428 };
      don.donneur = donneur;

      const donneurCollection: IDonneur[] = [{ id: 50864 }];
      jest.spyOn(donneurService, 'query').mockReturnValue(of(new HttpResponse({ body: donneurCollection })));
      const additionalDonneurs = [donneur];
      const expectedCollection: IDonneur[] = [...additionalDonneurs, ...donneurCollection];
      jest.spyOn(donneurService, 'addDonneurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ don });
      comp.ngOnInit();

      expect(donneurService.query).toHaveBeenCalled();
      expect(donneurService.addDonneurToCollectionIfMissing).toHaveBeenCalledWith(donneurCollection, ...additionalDonneurs);
      expect(comp.donneursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const don: IDon = { id: 456 };
      const donneur: IDonneur = { id: 10101 };
      don.donneur = donneur;

      activatedRoute.data = of({ don });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(don));
      expect(comp.donneursSharedCollection).toContain(donneur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Don>>();
      const don = { id: 123 };
      jest.spyOn(donService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ don });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: don }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(donService.update).toHaveBeenCalledWith(don);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Don>>();
      const don = new Don();
      jest.spyOn(donService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ don });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: don }));
      saveSubject.complete();

      // THEN
      expect(donService.create).toHaveBeenCalledWith(don);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Don>>();
      const don = { id: 123 };
      jest.spyOn(donService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ don });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donService.update).toHaveBeenCalledWith(don);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDonneurById', () => {
      it('Should return tracked Donneur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDonneurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
