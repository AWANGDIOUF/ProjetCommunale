jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VainqueurService } from '../service/vainqueur.service';
import { IVainqueur, Vainqueur } from '../vainqueur.model';

import { VainqueurUpdateComponent } from './vainqueur-update.component';

describe('Component Tests', () => {
  describe('Vainqueur Management Update Component', () => {
    let comp: VainqueurUpdateComponent;
    let fixture: ComponentFixture<VainqueurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vainqueurService: VainqueurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VainqueurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VainqueurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VainqueurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vainqueurService = TestBed.inject(VainqueurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vainqueur: IVainqueur = { id: 456 };

        activatedRoute.data = of({ vainqueur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vainqueur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vainqueur>>();
        const vainqueur = { id: 123 };
        jest.spyOn(vainqueurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vainqueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vainqueur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vainqueurService.update).toHaveBeenCalledWith(vainqueur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vainqueur>>();
        const vainqueur = new Vainqueur();
        jest.spyOn(vainqueurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vainqueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vainqueur }));
        saveSubject.complete();

        // THEN
        expect(vainqueurService.create).toHaveBeenCalledWith(vainqueur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vainqueur>>();
        const vainqueur = { id: 123 };
        jest.spyOn(vainqueurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vainqueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vainqueurService.update).toHaveBeenCalledWith(vainqueur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
