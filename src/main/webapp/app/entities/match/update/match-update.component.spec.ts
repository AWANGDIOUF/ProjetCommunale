jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MatchService } from '../service/match.service';
import { IMatch, Match } from '../match.model';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';

import { MatchUpdateComponent } from './match-update.component';

describe('Component Tests', () => {
  describe('Match Management Update Component', () => {
    let comp: MatchUpdateComponent;
    let fixture: ComponentFixture<MatchUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let matchService: MatchService;
    let equipeService: EquipeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MatchUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MatchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatchUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      matchService = TestBed.inject(MatchService);
      equipeService = TestBed.inject(EquipeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Equipe query and add missing value', () => {
        const match: IMatch = { id: 456 };
        const equipes: IEquipe[] = [{ id: 59352 }];
        match.equipes = equipes;

        const equipeCollection: IEquipe[] = [{ id: 47506 }];
        jest.spyOn(equipeService, 'query').mockReturnValue(of(new HttpResponse({ body: equipeCollection })));
        const additionalEquipes = [...equipes];
        const expectedCollection: IEquipe[] = [...additionalEquipes, ...equipeCollection];
        jest.spyOn(equipeService, 'addEquipeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(equipeService.query).toHaveBeenCalled();
        expect(equipeService.addEquipeToCollectionIfMissing).toHaveBeenCalledWith(equipeCollection, ...additionalEquipes);
        expect(comp.equipesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const match: IMatch = { id: 456 };
        const equipes: IEquipe = { id: 39260 };
        match.equipes = [equipes];

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(match));
        expect(comp.equipesSharedCollection).toContain(equipes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Match>>();
        const match = { id: 123 };
        jest.spyOn(matchService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: match }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(matchService.update).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Match>>();
        const match = new Match();
        jest.spyOn(matchService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: match }));
        saveSubject.complete();

        // THEN
        expect(matchService.create).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Match>>();
        const match = { id: 123 };
        jest.spyOn(matchService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(matchService.update).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEquipeById', () => {
        it('Should return tracked Equipe primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEquipeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedEquipe', () => {
        it('Should return option if no Equipe is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedEquipe(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Equipe for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedEquipe(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Equipe is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedEquipe(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
