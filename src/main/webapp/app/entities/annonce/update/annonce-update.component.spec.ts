jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnnonceService } from '../service/annonce.service';
import { IAnnonce, Annonce } from '../annonce.model';

import { AnnonceUpdateComponent } from './annonce-update.component';

describe('Component Tests', () => {
  describe('Annonce Management Update Component', () => {
    let comp: AnnonceUpdateComponent;
    let fixture: ComponentFixture<AnnonceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let annonceService: AnnonceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnonceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnnonceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnonceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      annonceService = TestBed.inject(AnnonceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const annonce: IAnnonce = { id: 456 };

        activatedRoute.data = of({ annonce });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(annonce));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Annonce>>();
        const annonce = { id: 123 };
        jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ annonce });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annonce }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(annonceService.update).toHaveBeenCalledWith(annonce);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Annonce>>();
        const annonce = new Annonce();
        jest.spyOn(annonceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ annonce });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: annonce }));
        saveSubject.complete();

        // THEN
        expect(annonceService.create).toHaveBeenCalledWith(annonce);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Annonce>>();
        const annonce = { id: 123 };
        jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ annonce });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(annonceService.update).toHaveBeenCalledWith(annonce);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
