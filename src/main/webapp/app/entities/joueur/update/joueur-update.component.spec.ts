jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JoueurService } from '../service/joueur.service';
import { IJoueur, Joueur } from '../joueur.model';

import { JoueurUpdateComponent } from './joueur-update.component';

describe('Component Tests', () => {
  describe('Joueur Management Update Component', () => {
    let comp: JoueurUpdateComponent;
    let fixture: ComponentFixture<JoueurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let joueurService: JoueurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JoueurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JoueurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JoueurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      joueurService = TestBed.inject(JoueurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const joueur: IJoueur = { id: 456 };

        activatedRoute.data = of({ joueur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(joueur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Joueur>>();
        const joueur = { id: 123 };
        jest.spyOn(joueurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ joueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: joueur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(joueurService.update).toHaveBeenCalledWith(joueur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Joueur>>();
        const joueur = new Joueur();
        jest.spyOn(joueurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ joueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: joueur }));
        saveSubject.complete();

        // THEN
        expect(joueurService.create).toHaveBeenCalledWith(joueur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Joueur>>();
        const joueur = { id: 123 };
        jest.spyOn(joueurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ joueur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(joueurService.update).toHaveBeenCalledWith(joueur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
