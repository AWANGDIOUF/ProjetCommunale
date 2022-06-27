import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JoueurService } from '../service/joueur.service';
import { IJoueur, Joueur } from '../joueur.model';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';

import { JoueurUpdateComponent } from './joueur-update.component';

describe('Joueur Management Update Component', () => {
  let comp: JoueurUpdateComponent;
  let fixture: ComponentFixture<JoueurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let joueurService: JoueurService;
  let equipeService: EquipeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JoueurUpdateComponent],
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
      .overrideTemplate(JoueurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JoueurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    joueurService = TestBed.inject(JoueurService);
    equipeService = TestBed.inject(EquipeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Equipe query and add missing value', () => {
      const joueur: IJoueur = { id: 456 };
      const equipe: IEquipe = { id: 13711 };
      joueur.equipe = equipe;

      const equipeCollection: IEquipe[] = [{ id: 21472 }];
      jest.spyOn(equipeService, 'query').mockReturnValue(of(new HttpResponse({ body: equipeCollection })));
      const additionalEquipes = [equipe];
      const expectedCollection: IEquipe[] = [...additionalEquipes, ...equipeCollection];
      jest.spyOn(equipeService, 'addEquipeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ joueur });
      comp.ngOnInit();

      expect(equipeService.query).toHaveBeenCalled();
      expect(equipeService.addEquipeToCollectionIfMissing).toHaveBeenCalledWith(equipeCollection, ...additionalEquipes);
      expect(comp.equipesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const joueur: IJoueur = { id: 456 };
      const equipe: IEquipe = { id: 34450 };
      joueur.equipe = equipe;

      activatedRoute.data = of({ joueur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(joueur));
      expect(comp.equipesSharedCollection).toContain(equipe);
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

  describe('Tracking relationships identifiers', () => {
    describe('trackEquipeById', () => {
      it('Should return tracked Equipe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEquipeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
