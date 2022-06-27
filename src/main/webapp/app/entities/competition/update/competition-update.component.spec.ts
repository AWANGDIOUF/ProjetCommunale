import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetitionService } from '../service/competition.service';
import { ICompetition, Competition } from '../competition.model';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

import { CompetitionUpdateComponent } from './competition-update.component';

describe('Competition Management Update Component', () => {
  let comp: CompetitionUpdateComponent;
  let fixture: ComponentFixture<CompetitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competitionService: CompetitionService;
  let clubService: ClubService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetitionUpdateComponent],
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
      .overrideTemplate(CompetitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competitionService = TestBed.inject(CompetitionService);
    clubService = TestBed.inject(ClubService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Club query and add missing value', () => {
      const competition: ICompetition = { id: 456 };
      const club: IClub = { id: 28296 };
      competition.club = club;

      const clubCollection: IClub[] = [{ id: 88998 }];
      jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
      const additionalClubs = [club];
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
      const club: IClub = { id: 70159 };
      competition.club = club;

      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(competition));
      expect(comp.clubsSharedCollection).toContain(club);
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
    describe('trackClubById', () => {
      it('Should return tracked Club primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClubById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
