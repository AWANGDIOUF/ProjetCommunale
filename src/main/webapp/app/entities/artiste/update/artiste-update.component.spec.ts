import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArtisteService } from '../service/artiste.service';
import { IArtiste, Artiste } from '../artiste.model';

import { ArtisteUpdateComponent } from './artiste-update.component';

describe('Artiste Management Update Component', () => {
  let comp: ArtisteUpdateComponent;
  let fixture: ComponentFixture<ArtisteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let artisteService: ArtisteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArtisteUpdateComponent],
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
      .overrideTemplate(ArtisteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArtisteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    artisteService = TestBed.inject(ArtisteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const artiste: IArtiste = { id: 456 };

      activatedRoute.data = of({ artiste });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(artiste));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Artiste>>();
      const artiste = { id: 123 };
      jest.spyOn(artisteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ artiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: artiste }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(artisteService.update).toHaveBeenCalledWith(artiste);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Artiste>>();
      const artiste = new Artiste();
      jest.spyOn(artisteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ artiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: artiste }));
      saveSubject.complete();

      // THEN
      expect(artisteService.create).toHaveBeenCalledWith(artiste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Artiste>>();
      const artiste = { id: 123 };
      jest.spyOn(artisteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ artiste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(artisteService.update).toHaveBeenCalledWith(artiste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
