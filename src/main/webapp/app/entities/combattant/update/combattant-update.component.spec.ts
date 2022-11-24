jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CombattantService } from '../service/combattant.service';
import { ICombattant, Combattant } from '../combattant.model';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { VainqueurService } from 'app/entities/vainqueur/service/vainqueur.service';

import { CombattantUpdateComponent } from './combattant-update.component';

describe('Component Tests', () => {
  describe('Combattant Management Update Component', () => {
    let comp: CombattantUpdateComponent;
    let fixture: ComponentFixture<CombattantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let combattantService: CombattantService;
    let vainqueurService: VainqueurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CombattantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CombattantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CombattantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      combattantService = TestBed.inject(CombattantService);
      vainqueurService = TestBed.inject(VainqueurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vainqueur query and add missing value', () => {
        const combattant: ICombattant = { id: 456 };
        const combattant: IVainqueur = { id: 3602 };
        combattant.combattant = combattant;

        const vainqueurCollection: IVainqueur[] = [{ id: 46896 }];
        jest.spyOn(vainqueurService, 'query').mockReturnValue(of(new HttpResponse({ body: vainqueurCollection })));
        const additionalVainqueurs = [combattant];
        const expectedCollection: IVainqueur[] = [...additionalVainqueurs, ...vainqueurCollection];
        jest.spyOn(vainqueurService, 'addVainqueurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ combattant });
        comp.ngOnInit();

        expect(vainqueurService.query).toHaveBeenCalled();
        expect(vainqueurService.addVainqueurToCollectionIfMissing).toHaveBeenCalledWith(vainqueurCollection, ...additionalVainqueurs);
        expect(comp.vainqueursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const combattant: ICombattant = { id: 456 };
        const combattant: IVainqueur = { id: 94908 };
        combattant.combattant = combattant;

        activatedRoute.data = of({ combattant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(combattant));
        expect(comp.vainqueursSharedCollection).toContain(combattant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Combattant>>();
        const combattant = { id: 123 };
        jest.spyOn(combattantService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ combattant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: combattant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(combattantService.update).toHaveBeenCalledWith(combattant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Combattant>>();
        const combattant = new Combattant();
        jest.spyOn(combattantService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ combattant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: combattant }));
        saveSubject.complete();

        // THEN
        expect(combattantService.create).toHaveBeenCalledWith(combattant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Combattant>>();
        const combattant = { id: 123 };
        jest.spyOn(combattantService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ combattant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(combattantService.update).toHaveBeenCalledWith(combattant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVainqueurById', () => {
        it('Should return tracked Vainqueur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVainqueurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
