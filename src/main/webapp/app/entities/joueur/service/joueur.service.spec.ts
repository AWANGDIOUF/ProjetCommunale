import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Poste } from 'app/entities/enumerations/poste.model';
import { IJoueur, Joueur } from '../joueur.model';

import { JoueurService } from './joueur.service';

describe('Joueur Service', () => {
  let service: JoueurService;
  let httpMock: HttpTestingController;
  let elemDefault: IJoueur;
  let expectedResult: IJoueur | IJoueur[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JoueurService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomJoueur: 'AAAAAAA',
      prenomJoueur: 'AAAAAAA',
      dateNaisJoueur: currentDate,
      lieuNaisJoueur: 'AAAAAAA',
      posteJoueur: Poste.ATTAQUANT,
      photoJoueurContentType: 'image/png',
      photoJoueur: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateNaisJoueur: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Joueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateNaisJoueur: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisJoueur: currentDate,
        },
        returnedFromService
      );

      service.create(new Joueur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Joueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomJoueur: 'BBBBBB',
          prenomJoueur: 'BBBBBB',
          dateNaisJoueur: currentDate.format(DATE_FORMAT),
          lieuNaisJoueur: 'BBBBBB',
          posteJoueur: 'BBBBBB',
          photoJoueur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisJoueur: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Joueur', () => {
      const patchObject = Object.assign(
        {
          lieuNaisJoueur: 'BBBBBB',
        },
        new Joueur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateNaisJoueur: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Joueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomJoueur: 'BBBBBB',
          prenomJoueur: 'BBBBBB',
          dateNaisJoueur: currentDate.format(DATE_FORMAT),
          lieuNaisJoueur: 'BBBBBB',
          posteJoueur: 'BBBBBB',
          photoJoueur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisJoueur: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Joueur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addJoueurToCollectionIfMissing', () => {
      it('should add a Joueur to an empty array', () => {
        const joueur: IJoueur = { id: 123 };
        expectedResult = service.addJoueurToCollectionIfMissing([], joueur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(joueur);
      });

      it('should not add a Joueur to an array that contains it', () => {
        const joueur: IJoueur = { id: 123 };
        const joueurCollection: IJoueur[] = [
          {
            ...joueur,
          },
          { id: 456 },
        ];
        expectedResult = service.addJoueurToCollectionIfMissing(joueurCollection, joueur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Joueur to an array that doesn't contain it", () => {
        const joueur: IJoueur = { id: 123 };
        const joueurCollection: IJoueur[] = [{ id: 456 }];
        expectedResult = service.addJoueurToCollectionIfMissing(joueurCollection, joueur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(joueur);
      });

      it('should add only unique Joueur to an array', () => {
        const joueurArray: IJoueur[] = [{ id: 123 }, { id: 456 }, { id: 25073 }];
        const joueurCollection: IJoueur[] = [{ id: 123 }];
        expectedResult = service.addJoueurToCollectionIfMissing(joueurCollection, ...joueurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const joueur: IJoueur = { id: 123 };
        const joueur2: IJoueur = { id: 456 };
        expectedResult = service.addJoueurToCollectionIfMissing([], joueur, joueur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(joueur);
        expect(expectedResult).toContain(joueur2);
      });

      it('should accept null and undefined values', () => {
        const joueur: IJoueur = { id: 123 };
        expectedResult = service.addJoueurToCollectionIfMissing([], null, joueur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(joueur);
      });

      it('should return initial array if no Joueur is added', () => {
        const joueurCollection: IJoueur[] = [{ id: 123 }];
        expectedResult = service.addJoueurToCollectionIfMissing(joueurCollection, undefined, null);
        expect(expectedResult).toEqual(joueurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
