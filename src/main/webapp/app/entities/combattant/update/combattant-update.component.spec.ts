import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CombattantService } from '../service/combattant.service';
import { ICombattant, Combattant } from '../combattant.model';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

import { CombattantUpdateComponent } from './combattant-update.component';

describe('Combattant Management Update Component', () => {
  let comp: CombattantUpdateComponent;
  let fixture: ComponentFixture<CombattantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let combattantService: CombattantService;
  let clubService: ClubService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CombattantUpdateComponent],
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
      .overrideTemplate(CombattantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CombattantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    combattantService = TestBed.inject(CombattantService);
    clubService = TestBed.inject(ClubService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Club query and add missing value', () => {
      const combattant: ICombattant = { id: 456 };
      const club: IClub = { id: 59403 };
      combattant.club = club;

      const clubCollection: IClub[] = [{ id: 38901 }];
      jest.spyOn(clubService, 'query').mockReturnValue(of(new HttpResponse({ body: clubCollection })));
      const additionalClubs = [club];
      const expectedCollection: IClub[] = [...additionalClubs, ...clubCollection];
      jest.spyOn(clubService, 'addClubToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ combattant });
      comp.ngOnInit();

      expect(clubService.query).toHaveBeenCalled();
      expect(clubService.addClubToCollectionIfMissing).toHaveBeenCalledWith(clubCollection, ...additionalClubs);
      expect(comp.clubsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const combattant: ICombattant = { id: 456 };
      const club: IClub = { id: 11080 };
      combattant.club = club;

      activatedRoute.data = of({ combattant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(combattant));
      expect(comp.clubsSharedCollection).toContain(club);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Combattant>>();
      const combattant = { id: 123 };
      jest.spyOn(combattantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combattant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: combattant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(combattantService.update).toHaveBeenCalledWith(combattant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Combattant>>();
      const combattant = new Combattant();
      jest.spyOn(combattantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combattant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: combattant }));
      saveSubject.complete();

      // THEN
      expect(combattantService.create).toHaveBeenCalledWith(combattant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Combattant>>();
      const combattant = { id: 123 };
      jest.spyOn(combattantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ combattant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(combattantService.update).toHaveBeenCalledWith(combattant);
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
