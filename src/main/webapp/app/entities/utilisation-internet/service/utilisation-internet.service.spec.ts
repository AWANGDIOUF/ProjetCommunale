import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Profil } from 'app/entities/enumerations/profil.model';
import { Domaine } from 'app/entities/enumerations/domaine.model';
import { IUtilisationInternet, UtilisationInternet } from '../utilisation-internet.model';

import { UtilisationInternetService } from './utilisation-internet.service';

describe('UtilisationInternet Service', () => {
  let service: UtilisationInternetService;
  let httpMock: HttpTestingController;
  let elemDefault: IUtilisationInternet;
  let expectedResult: IUtilisationInternet | IUtilisationInternet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtilisationInternetService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      profil: Profil.Commercant,
      autre: 'AAAAAAA',
      domaine: Domaine.Chanteurs,
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a UtilisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UtilisationInternet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UtilisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          profil: 'BBBBBB',
          autre: 'BBBBBB',
          domaine: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UtilisationInternet', () => {
      const patchObject = Object.assign(
        {
          profil: 'BBBBBB',
          domaine: 'BBBBBB',
        },
        new UtilisationInternet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UtilisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          profil: 'BBBBBB',
          autre: 'BBBBBB',
          domaine: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a UtilisationInternet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUtilisationInternetToCollectionIfMissing', () => {
      it('should add a UtilisationInternet to an empty array', () => {
        const utilisationInternet: IUtilisationInternet = { id: 123 };
        expectedResult = service.addUtilisationInternetToCollectionIfMissing([], utilisationInternet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilisationInternet);
      });

      it('should not add a UtilisationInternet to an array that contains it', () => {
        const utilisationInternet: IUtilisationInternet = { id: 123 };
        const utilisationInternetCollection: IUtilisationInternet[] = [
          {
            ...utilisationInternet,
          },
          { id: 456 },
        ];
        expectedResult = service.addUtilisationInternetToCollectionIfMissing(utilisationInternetCollection, utilisationInternet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UtilisationInternet to an array that doesn't contain it", () => {
        const utilisationInternet: IUtilisationInternet = { id: 123 };
        const utilisationInternetCollection: IUtilisationInternet[] = [{ id: 456 }];
        expectedResult = service.addUtilisationInternetToCollectionIfMissing(utilisationInternetCollection, utilisationInternet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilisationInternet);
      });

      it('should add only unique UtilisationInternet to an array', () => {
        const utilisationInternetArray: IUtilisationInternet[] = [{ id: 123 }, { id: 456 }, { id: 32223 }];
        const utilisationInternetCollection: IUtilisationInternet[] = [{ id: 123 }];
        expectedResult = service.addUtilisationInternetToCollectionIfMissing(utilisationInternetCollection, ...utilisationInternetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utilisationInternet: IUtilisationInternet = { id: 123 };
        const utilisationInternet2: IUtilisationInternet = { id: 456 };
        expectedResult = service.addUtilisationInternetToCollectionIfMissing([], utilisationInternet, utilisationInternet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilisationInternet);
        expect(expectedResult).toContain(utilisationInternet2);
      });

      it('should accept null and undefined values', () => {
        const utilisationInternet: IUtilisationInternet = { id: 123 };
        expectedResult = service.addUtilisationInternetToCollectionIfMissing([], null, utilisationInternet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilisationInternet);
      });

      it('should return initial array if no UtilisationInternet is added', () => {
        const utilisationInternetCollection: IUtilisationInternet[] = [{ id: 123 }];
        expectedResult = service.addUtilisationInternetToCollectionIfMissing(utilisationInternetCollection, undefined, null);
        expect(expectedResult).toEqual(utilisationInternetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
