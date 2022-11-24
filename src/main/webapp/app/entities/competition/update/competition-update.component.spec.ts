jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompetitionService } from '../service/competition.service';
import { ICompetition, Competition } from '../competition.model';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { VainqueurService } from 'app/entities/vainqueur/service/vainqueur.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

import { CompetitionUpdateComponent } from './competition-update.component';

describe('Component Tests', () => {
  describe('Competition Management Update Component', () => {
    let comp: CompetitionUpdateComponent;
    let fixture: ComponentFixture<CompetitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let competitionService: CompetitionService;
    let vainqueurService: VainqueurService;
    let clubService: ClubService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompetitionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompetitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompetitionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      competitionService = TestBed.inject(CompetitionService);
      vainqueurService = TestBed.inject(VainqueurService);
      clubService = TestBed.inject(ClubService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vainqueur query and add missing value', () => {
        const competition: ICompetition = { id: 456 };
        const vainqueur: IVainqueur = { id: 89965 };
        competition.vainqueur = vainqueur;

        const vainqueurCollection: IVainqueur[] = [{ id: 74274 }];
        jest.spyOn(vainqueurService, 'query').mockReturnValue(of(new HttpResponse({ body: vainqueurCollection })));
        const additionalVainqueurs = [vainqueur];
        const expectedCollection: IVainqueur[] = [...additionalVainqueurs, ...vainqueurCollection];
        jest.spyOn(vainqueurService, 'addVainqueurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        expect(vainqueurService.query).toHaveBeenCalled();
        expect(vainqueurService.addVainqueurToCollectionIfMissing).toHaveBeenCalledWith(vainqueurCollection, ...additionalVainqueurs);
        expect(comp.vainqueursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Club query and add missing value', () => {
        const competition: ICompetition = { id: 456 };
        const clubs: IClub[] = [{ id: 28296 }];
        competition.clubs = clubs;

        const clubCollection: IClub[] = [{ id: 88998 }];
        jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
        const additionalClubs = [...clubs];
        const expectedCollection: IClub[] = [...additionalClubs, ...clubCollection];
        jest.spyOn(clubService, 'addClubToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        expect(clubService.query).toHaveBeenCalled();
        expect(clubService.addClubToCollectionIfMissing).toHaveBeenCalledWith(clubCollection, ...additionalClubs);
        expect(comp.clubsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const competition: ICompetition = { id: 456 };
        const vainqueur: IVainqueur = { id: 14869 };
        competition.vainqueur = vainqueur;
        const clubs: IClub = { id: 70159 };
        competition.clubs = [clubs];

        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(competition));
        expect(comp.vainqueursSharedCollection).toContain(vainqueur);
        expect(comp.clubsSharedCollection).toContain(clubs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Competition>>();
        const competition = { id: 123 };
        jest.spyOn(competitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: competition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(competitionService.update).toHaveBeenCalledWith(competition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Competition>>();
        const competition = new Competition();
        jest.spyOn(competitionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: competition }));
        saveSubject.complete();

        // THEN
        expect(competitionService.create).toHaveBeenCalledWith(competition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Competition>>();
        const competition = { id: 123 };
        jest.spyOn(competitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ competition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(competitionService.update).toHaveBeenCalledWith(competition);
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

      describe('trackClubById', () => {
        it('Should return tracked Club primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClubById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedClub', () => {
        it('Should return option if no Club is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedClub(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Club for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedClub(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Club is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedClub(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
