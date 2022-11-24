jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EquipeService } from '../service/equipe.service';
import { IEquipe, Equipe } from '../equipe.model';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { IJoueur } from 'app/entities/joueur/joueur.model';
import { JoueurService } from 'app/entities/joueur/service/joueur.service';

import { EquipeUpdateComponent } from './equipe-update.component';

describe('Component Tests', () => {
  describe('Equipe Management Update Component', () => {
    let comp: EquipeUpdateComponent;
    let fixture: ComponentFixture<EquipeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let equipeService: EquipeService;
    let typeSportService: TypeSportService;
    let joueurService: JoueurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EquipeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EquipeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EquipeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      equipeService = TestBed.inject(EquipeService);
      typeSportService = TestBed.inject(TypeSportService);
      joueurService = TestBed.inject(JoueurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call typeSport query and add missing value', () => {
        const equipe: IEquipe = { id: 456 };
        const typeSport: ITypeSport = { id: 46305 };
        equipe.typeSport = typeSport;

        const typeSportCollection: ITypeSport[] = [{ id: 54828 }];
        jest.spyOn(typeSportService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSportCollection })));
        const expectedCollection: ITypeSport[] = [typeSport, ...typeSportCollection];
        jest.spyOn(typeSportService, 'addTypeSportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        expect(typeSportService.query).toHaveBeenCalled();
        expect(typeSportService.addTypeSportToCollectionIfMissing).toHaveBeenCalledWith(typeSportCollection, typeSport);
        expect(comp.typeSportsCollection).toEqual(expectedCollection);
      });

      it('Should call Joueur query and add missing value', () => {
        const equipe: IEquipe = { id: 456 };
        const joueur: IJoueur = { id: 69802 };
        equipe.joueur = joueur;

        const joueurCollection: IJoueur[] = [{ id: 65518 }];
        jest.spyOn(joueurService, 'query').mockReturnValue(of(new HttpResponse({ body: joueurCollection })));
        const additionalJoueurs = [joueur];
        const expectedCollection: IJoueur[] = [...additionalJoueurs, ...joueurCollection];
        jest.spyOn(joueurService, 'addJoueurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        expect(joueurService.query).toHaveBeenCalled();
        expect(joueurService.addJoueurToCollectionIfMissing).toHaveBeenCalledWith(joueurCollection, ...additionalJoueurs);
        expect(comp.joueursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const equipe: IEquipe = { id: 456 };
        const typeSport: ITypeSport = { id: 76416 };
        equipe.typeSport = typeSport;
        const joueur: IJoueur = { id: 9963 };
        equipe.joueur = joueur;

        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(equipe));
        expect(comp.typeSportsCollection).toContain(typeSport);
        expect(comp.joueursSharedCollection).toContain(joueur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Equipe>>();
        const equipe = { id: 123 };
        jest.spyOn(equipeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipe }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(equipeService.update).toHaveBeenCalledWith(equipe);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Equipe>>();
        const equipe = new Equipe();
        jest.spyOn(equipeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipe }));
        saveSubject.complete();

        // THEN
        expect(equipeService.create).toHaveBeenCalledWith(equipe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Equipe>>();
        const equipe = { id: 123 };
        jest.spyOn(equipeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(equipeService.update).toHaveBeenCalledWith(equipe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTypeSportById', () => {
        it('Should return tracked TypeSport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTypeSportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackJoueurById', () => {
        it('Should return tracked Joueur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackJoueurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
