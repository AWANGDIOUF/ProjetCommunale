import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VainqueurService } from '../service/vainqueur.service';
import { IVainqueur, Vainqueur } from '../vainqueur.model';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';
import { ICombattant } from 'app/entities/combattant/combattant.model';
import { CombattantService } from 'app/entities/combattant/service/combattant.service';

import { VainqueurUpdateComponent } from './vainqueur-update.component';

describe('Vainqueur Management Update Component', () => {
  let comp: VainqueurUpdateComponent;
  let fixture: ComponentFixture<VainqueurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vainqueurService: VainqueurService;
  let competitionService: CompetitionService;
  let combattantService: CombattantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VainqueurUpdateComponent],
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
      .overrideTemplate(VainqueurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VainqueurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vainqueurService = TestBed.inject(VainqueurService);
    competitionService = TestBed.inject(CompetitionService);
    combattantService = TestBed.inject(CombattantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Competition query and add missing value', () => {
      const vainqueur: IVainqueur = { id: 456 };
      const competition: ICompetition = { id: 46882 };
      vainqueur.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 55653 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vainqueur });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Combattant query and add missing value', () => {
      const vainqueur: IVainqueur = { id: 456 };
      const combattant: ICombattant = { id: 49996 };
      vainqueur.combattant = combattant;

      const combattantCollection: ICombattant[] = [{ id: 77593 }];
      jest.spyOn(combattantService, 'query').mockReturnValue(of(new HttpResponse({ body: combattantCollection })));
      const additionalCombattants = [combattant];
      const expectedCollection: ICombattant[] = [...additionalCombattants, ...combattantCollection];
      jest.spyOn(combattantService, 'addCombattantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vainqueur });
      comp.ngOnInit();

      expect(combattantService.query).toHaveBeenCalled();
      expect(combattantService.addCombattantToCollectionIfMissing).toHaveBeenCalledWith(combattantCollection, ...additionalCombattants);
      expect(comp.combattantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vainqueur: IVainqueur = { id: 456 };
      const competition: ICompetition = { id: 23007 };
      vainqueur.competition = competition;
      const combattant: ICombattant = { id: 88735 };
      vainqueur.combattant = combattant;

      activatedRoute.data = of({ vainqueur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vainqueur));
      expect(comp.competitionsSharedCollection).toContain(competition);
      expect(comp.combattantsSharedCollection).toContain(combattant);
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

  describe('Tracking relationships identifiers', () => {
    describe('trackCompetitionById', () => {
      it('Should return tracked Competition primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetitionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCombattantById', () => {
      it('Should return tracked Combattant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCombattantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
