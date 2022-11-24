jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ArchiveSportService } from '../service/archive-sport.service';
import { IArchiveSport, ArchiveSport } from '../archive-sport.model';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

import { ArchiveSportUpdateComponent } from './archive-sport-update.component';

describe('Component Tests', () => {
  describe('ArchiveSport Management Update Component', () => {
    let comp: ArchiveSportUpdateComponent;
    let fixture: ComponentFixture<ArchiveSportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let archiveSportService: ArchiveSportService;
    let equipeService: EquipeService;
    let clubService: ClubService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ArchiveSportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ArchiveSportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArchiveSportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      archiveSportService = TestBed.inject(ArchiveSportService);
      equipeService = TestBed.inject(EquipeService);
      clubService = TestBed.inject(ClubService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Equipe query and add missing value', () => {
        const archiveSport: IArchiveSport = { id: 456 };
        const equipes: IEquipe[] = [{ id: 1799 }];
        archiveSport.equipes = equipes;

        const equipeCollection: IEquipe[] = [{ id: 53807 }];
        jest.spyOn(equipeService, 'query').mockReturnValue(of(new HttpResponse({ body: equipeCollection })));
        const additionalEquipes = [...equipes];
        const expectedCollection: IEquipe[] = [...additionalEquipes, ...equipeCollection];
        jest.spyOn(equipeService, 'addEquipeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        expect(equipeService.query).toHaveBeenCalled();
        expect(equipeService.addEquipeToCollectionIfMissing).toHaveBeenCalledWith(equipeCollection, ...additionalEquipes);
        expect(comp.equipesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Club query and add missing value', () => {
        const archiveSport: IArchiveSport = { id: 456 };
        const clubs: IClub[] = [{ id: 31265 }];
        archiveSport.clubs = clubs;

        const clubCollection: IClub[] = [{ id: 67382 }];
        jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
        const additionalClubs = [...clubs];
        const expectedCollection: IClub[] = [...additionalClubs, ...clubCollection];
        jest.spyOn(clubService, 'addClubToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        expect(clubService.query).toHaveBeenCalled();
        expect(clubService.addClubToCollectionIfMissing).toHaveBeenCalledWith(clubCollection, ...additionalClubs);
        expect(comp.clubsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const archiveSport: IArchiveSport = { id: 456 };
        const equipes: IEquipe = { id: 81265 };
        archiveSport.equipes = [equipes];
        const clubs: IClub = { id: 25805 };
        archiveSport.clubs = [clubs];

        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(archiveSport));
        expect(comp.equipesSharedCollection).toContain(equipes);
        expect(comp.clubsSharedCollection).toContain(clubs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ArchiveSport>>();
        const archiveSport = { id: 123 };
        jest.spyOn(archiveSportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: archiveSport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(archiveSportService.update).toHaveBeenCalledWith(archiveSport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ArchiveSport>>();
        const archiveSport = new ArchiveSport();
        jest.spyOn(archiveSportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: archiveSport }));
        saveSubject.complete();

        // THEN
        expect(archiveSportService.create).toHaveBeenCalledWith(archiveSport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ArchiveSport>>();
        const archiveSport = { id: 123 };
        jest.spyOn(archiveSportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ archiveSport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(archiveSportService.update).toHaveBeenCalledWith(archiveSport);
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

      describe('trackClubById', () => {
        it('Should return tracked Club primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClubById(0, entity);
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
