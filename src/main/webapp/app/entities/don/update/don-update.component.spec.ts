jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DonService } from '../service/don.service';
import { IDon, Don } from '../don.model';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

import { DonUpdateComponent } from './don-update.component';

describe('Component Tests', () => {
  describe('Don Management Update Component', () => {
    let comp: DonUpdateComponent;
    let fixture: ComponentFixture<DonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let donService: DonService;
    let annonceService: AnnonceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      donService = TestBed.inject(DonService);
      annonceService = TestBed.inject(AnnonceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Annonce query and add missing value', () => {
        const don: IDon = { id: 456 };
        const annonce: IAnnonce = { id: 98870 };
        don.annonce = annonce;

        const annonceCollection: IAnnonce[] = [{ id: 84003 }];
        jest.spyOn(annonceService, 'query').mockReturnValue(of(new HttpResponse({ body: annonceCollection })));
        const additionalAnnonces = [annonce];
        const expectedCollection: IAnnonce[] = [...additionalAnnonces, ...annonceCollection];
        jest.spyOn(annonceService, 'addAnnonceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ don });
        comp.ngOnInit();

        expect(annonceService.query).toHaveBeenCalled();
        expect(annonceService.addAnnonceToCollectionIfMissing).toHaveBeenCalledWith(annonceCollection, ...additionalAnnonces);
        expect(comp.annoncesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const don: IDon = { id: 456 };
        const annonce: IAnnonce = { id: 94002 };
        don.annonce = annonce;

        activatedRoute.data = of({ don });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(don));
        expect(comp.annoncesSharedCollection).toContain(annonce);
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
      describe('trackAnnonceById', () => {
        it('Should return tracked Annonce primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAnnonceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
