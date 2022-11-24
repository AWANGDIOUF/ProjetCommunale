jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DonSangService } from '../service/don-sang.service';
import { IDonSang, DonSang } from '../don-sang.model';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';
import { IDonneur } from 'app/entities/donneur/donneur.model';
import { DonneurService } from 'app/entities/donneur/service/donneur.service';

import { DonSangUpdateComponent } from './don-sang-update.component';

describe('Component Tests', () => {
  describe('DonSang Management Update Component', () => {
    let comp: DonSangUpdateComponent;
    let fixture: ComponentFixture<DonSangUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let donSangService: DonSangService;
    let annonceService: AnnonceService;
    let donneurService: DonneurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DonSangUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DonSangUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonSangUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      donSangService = TestBed.inject(DonSangService);
      annonceService = TestBed.inject(AnnonceService);
      donneurService = TestBed.inject(DonneurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Annonce query and add missing value', () => {
        const donSang: IDonSang = { id: 456 };
        const annonce: IAnnonce = { id: 22213 };
        donSang.annonce = annonce;

        const annonceCollection: IAnnonce[] = [{ id: 39134 }];
        jest.spyOn(annonceService, 'query').mockReturnValue(of(new HttpResponse({ body: annonceCollection })));
        const additionalAnnonces = [annonce];
        const expectedCollection: IAnnonce[] = [...additionalAnnonces, ...annonceCollection];
        jest.spyOn(annonceService, 'addAnnonceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        expect(annonceService.query).toHaveBeenCalled();
        expect(annonceService.addAnnonceToCollectionIfMissing).toHaveBeenCalledWith(annonceCollection, ...additionalAnnonces);
        expect(comp.annoncesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Donneur query and add missing value', () => {
        const donSang: IDonSang = { id: 456 };
        const donneur: IDonneur = { id: 92946 };
        donSang.donneur = donneur;

        const donneurCollection: IDonneur[] = [{ id: 90798 }];
        jest.spyOn(donneurService, 'query').mockReturnValue(of(new HttpResponse({ body: donneurCollection })));
        const additionalDonneurs = [donneur];
        const expectedCollection: IDonneur[] = [...additionalDonneurs, ...donneurCollection];
        jest.spyOn(donneurService, 'addDonneurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        expect(donneurService.query).toHaveBeenCalled();
        expect(donneurService.addDonneurToCollectionIfMissing).toHaveBeenCalledWith(donneurCollection, ...additionalDonneurs);
        expect(comp.donneursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const donSang: IDonSang = { id: 456 };
        const annonce: IAnnonce = { id: 22337 };
        donSang.annonce = annonce;
        const donneur: IDonneur = { id: 20270 };
        donSang.donneur = donneur;

        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(donSang));
        expect(comp.annoncesSharedCollection).toContain(annonce);
        expect(comp.donneursSharedCollection).toContain(donneur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DonSang>>();
        const donSang = { id: 123 };
        jest.spyOn(donSangService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: donSang }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(donSangService.update).toHaveBeenCalledWith(donSang);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DonSang>>();
        const donSang = new DonSang();
        jest.spyOn(donSangService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: donSang }));
        saveSubject.complete();

        // THEN
        expect(donSangService.create).toHaveBeenCalledWith(donSang);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DonSang>>();
        const donSang = { id: 123 };
        jest.spyOn(donSangService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ donSang });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(donSangService.update).toHaveBeenCalledWith(donSang);
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

      describe('trackDonneurById', () => {
        it('Should return tracked Donneur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDonneurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
