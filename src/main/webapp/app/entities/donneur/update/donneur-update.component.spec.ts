jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DonneurService } from '../service/donneur.service';
import { IDonneur, Donneur } from '../donneur.model';
import { IDon } from 'app/entities/don/don.model';
import { DonService } from 'app/entities/don/service/don.service';

import { DonneurUpdateComponent } from './donneur-update.component';

describe('Component Tests', () => {
  describe('Donneur Management Update Component', () => {
    let comp: DonneurUpdateComponent;
    let fixture: ComponentFixture<DonneurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let donneurService: DonneurService;
    let donService: DonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DonneurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DonneurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonneurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      donneurService = TestBed.inject(DonneurService);
      donService = TestBed.inject(DonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Don query and add missing value', () => {
        const donneur: IDonneur = { id: 456 };
        const don: IDon = { id: 28070 };
        donneur.don = don;

        const donCollection: IDon[] = [{ id: 85802 }];
        jest.spyOn(donService, 'query').mockReturnValue(of(new HttpResponse({ body: donCollection })));
        const additionalDons = [don];
        const expectedCollection: IDon[] = [...additionalDons, ...donCollection];
        jest.spyOn(donService, 'addDonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ donneur });
        comp.ngOnInit();

        expect(donService.query).toHaveBeenCalled();
        expect(donService.addDonToCollectionIfMissing).toHaveBeenCalledWith(donCollection, ...additionalDons);
        expect(comp.donsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const donneur: IDonneur = { id: 456 };
        const don: IDon = { id: 26043 };
        donneur.don = don;

        activatedRoute.data = of({ donneur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(donneur));
        expect(comp.donsSharedCollection).toContain(don);
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

    describe('Tracking relationships identifiers', () => {
      describe('trackDonById', () => {
        it('Should return tracked Don primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
