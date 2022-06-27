import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClubService } from '../service/club.service';
import { IClub, Club } from '../club.model';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { ClubUpdateComponent } from './club-update.component';

describe('Club Management Update Component', () => {
  let comp: ClubUpdateComponent;
  let fixture: ComponentFixture<ClubUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clubService: ClubService;
  let typeSportService: TypeSportService;
  let quartierService: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClubUpdateComponent],
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
      .overrideTemplate(ClubUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClubUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clubService = TestBed.inject(ClubService);
    typeSportService = TestBed.inject(TypeSportService);
    quartierService = TestBed.inject(QuartierService);

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

    it('Should call Quartier query and add missing value', () => {
      const club: IClub = { id: 456 };
      const quartier: IQuartier = { id: 96128 };
      club.quartier = quartier;

      const quartierCollection: IQuartier[] = [{ id: 24703 }];
      jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
      const additionalQuartiers = [quartier];
      const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
      jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ club });
      comp.ngOnInit();

      expect(quartierService.query).toHaveBeenCalled();
      expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
      expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const club: IClub = { id: 456 };
      const typeSport: ITypeSport = { id: 50303 };
      club.typeSport = typeSport;
      const quartier: IQuartier = { id: 28597 };
      club.quartier = quartier;

      activatedRoute.data = of({ club });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(club));
      expect(comp.typeSportsCollection).toContain(typeSport);
      expect(comp.quartiersSharedCollection).toContain(quartier);
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

    describe('trackQuartierById', () => {
      it('Should return tracked Quartier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuartierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
