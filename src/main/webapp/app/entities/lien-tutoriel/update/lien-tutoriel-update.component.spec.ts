import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LienTutorielService } from '../service/lien-tutoriel.service';
import { ILienTutoriel, LienTutoriel } from '../lien-tutoriel.model';
import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';
import { EnsegnantService } from 'app/entities/ensegnant/service/ensegnant.service';

import { LienTutorielUpdateComponent } from './lien-tutoriel-update.component';

describe('LienTutoriel Management Update Component', () => {
  let comp: LienTutorielUpdateComponent;
  let fixture: ComponentFixture<LienTutorielUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lienTutorielService: LienTutorielService;
  let ensegnantService: EnsegnantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LienTutorielUpdateComponent],
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
      .overrideTemplate(LienTutorielUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LienTutorielUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lienTutorielService = TestBed.inject(LienTutorielService);
    ensegnantService = TestBed.inject(EnsegnantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ensegnant query and add missing value', () => {
      const lienTutoriel: ILienTutoriel = { id: 456 };
      const enseignant: IEnsegnant = { id: 71753 };
      lienTutoriel.enseignant = enseignant;

      const ensegnantCollection: IEnsegnant[] = [{ id: 89875 }];
      jest.spyOn(ensegnantService, 'query').mockReturnValue(of(new HttpResponse({ body: ensegnantCollection })));
      const additionalEnsegnants = [enseignant];
      const expectedCollection: IEnsegnant[] = [...additionalEnsegnants, ...ensegnantCollection];
      jest.spyOn(ensegnantService, 'addEnsegnantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lienTutoriel });
      comp.ngOnInit();

      expect(ensegnantService.query).toHaveBeenCalled();
      expect(ensegnantService.addEnsegnantToCollectionIfMissing).toHaveBeenCalledWith(ensegnantCollection, ...additionalEnsegnants);
      expect(comp.ensegnantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lienTutoriel: ILienTutoriel = { id: 456 };
      const enseignant: IEnsegnant = { id: 35955 };
      lienTutoriel.enseignant = enseignant;

      activatedRoute.data = of({ lienTutoriel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lienTutoriel));
      expect(comp.ensegnantsSharedCollection).toContain(enseignant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LienTutoriel>>();
      const lienTutoriel = { id: 123 };
      jest.spyOn(lienTutorielService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lienTutoriel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lienTutoriel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lienTutorielService.update).toHaveBeenCalledWith(lienTutoriel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LienTutoriel>>();
      const lienTutoriel = new LienTutoriel();
      jest.spyOn(lienTutorielService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lienTutoriel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lienTutoriel }));
      saveSubject.complete();

      // THEN
      expect(lienTutorielService.create).toHaveBeenCalledWith(lienTutoriel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LienTutoriel>>();
      const lienTutoriel = { id: 123 };
      jest.spyOn(lienTutorielService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lienTutoriel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lienTutorielService.update).toHaveBeenCalledWith(lienTutoriel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEnsegnantById', () => {
      it('Should return tracked Ensegnant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnsegnantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
