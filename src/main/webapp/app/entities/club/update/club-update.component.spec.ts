jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClubService } from '../service/club.service';
import { IClub, Club } from '../club.model';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { ICombattant } from 'app/entities/combattant/combattant.model';
import { CombattantService } from 'app/entities/combattant/service/combattant.service';

import { ClubUpdateComponent } from './club-update.component';

describe('Component Tests', () => {
  describe('Club Management Update Component', () => {
    let comp: ClubUpdateComponent;
    let fixture: ComponentFixture<ClubUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clubService: ClubService;
    let typeSportService: TypeSportService;
    let combattantService: CombattantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClubUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClubUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClubUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clubService = TestBed.inject(ClubService);
      typeSportService = TestBed.inject(TypeSportService);
      combattantService = TestBed.inject(CombattantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call typeSport query and add missing value', () => {
        const club: IClub = { id: 456 };
        const typeSport: ITypeSport = { id: 91393 };
        club.typeSport = typeSport;

        const typeSportCollection: ITypeSport[] = [{ id: 13343 }];
        jest.spyOn(typeSportService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSportCollection })));
        const expectedCollection: ITypeSport[] = [typeSport, ...typeSportCollection];
        jest.spyOn(typeSportService, 'addTypeSportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ club });
        comp.ngOnInit();

        expect(typeSportService.query).toHaveBeenCalled();
        expect(typeSportService.addTypeSportToCollectionIfMissing).toHaveBeenCalledWith(typeSportCollection, typeSport);
        expect(comp.typeSportsCollection).toEqual(expectedCollection);
      });

      it('Should call Combattant query and add missing value', () => {
        const club: IClub = { id: 456 };
        const conmbattant: ICombattant = { id: 26116 };
        club.conmbattant = conmbattant;

        const combattantCollection: ICombattant[] = [{ id: 60446 }];
        jest.spyOn(combattantService, 'query').mockReturnValue(of(new HttpResponse({ body: combattantCollection })));
        const additionalCombattants = [conmbattant];
        const expectedCollection: ICombattant[] = [...additionalCombattants, ...combattantCollection];
        jest.spyOn(combattantService, 'addCombattantToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ club });
        comp.ngOnInit();

        expect(combattantService.query).toHaveBeenCalled();
        expect(combattantService.addCombattantToCollectionIfMissing).toHaveBeenCalledWith(combattantCollection, ...additionalCombattants);
        expect(comp.combattantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const club: IClub = { id: 456 };
        const typeSport: ITypeSport = { id: 50303 };
        club.typeSport = typeSport;
        const conmbattant: ICombattant = { id: 59928 };
        club.conmbattant = conmbattant;

        activatedRoute.data = of({ club });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(club));
        expect(comp.typeSportsCollection).toContain(typeSport);
        expect(comp.combattantsSharedCollection).toContain(conmbattant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Club>>();
        const club = { id: 123 };
        jest.spyOn(clubService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ club });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: club }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clubService.update).toHaveBeenCalledWith(club);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Club>>();
        const club = new Club();
        jest.spyOn(clubService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ club });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: club }));
        saveSubject.complete();

        // THEN
        expect(clubService.create).toHaveBeenCalledWith(club);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Club>>();
        const club = { id: 123 };
        jest.spyOn(clubService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ club });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clubService.update).toHaveBeenCalledWith(club);
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

      describe('trackCombattantById', () => {
        it('Should return tracked Combattant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCombattantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
