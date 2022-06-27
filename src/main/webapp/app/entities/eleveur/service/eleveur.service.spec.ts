import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeElevage } from 'app/entities/enumerations/type-elevage.model';
import { IEleveur, Eleveur } from '../eleveur.model';

import { EleveurService } from './eleveur.service';

describe('Eleveur Service', () => {
  let service: EleveurService;
  let httpMock: HttpTestingController;
  let elemDefault: IEleveur;
  let expectedResult: IEleveur | IEleveur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EleveurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomEleveur: 'AAAAAAA',
      prenomEleveur: 'AAAAAAA',
      telEleveur: 'AAAAAAA',
      tel1Eleveur: 'AAAAAAA',
      adresse: 'AAAAAAA',
      nomElevage: TypeElevage.Moutons,
      descriptionActivite: 'AAAAAAA',
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

    it('should create a Eleveur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Eleveur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Eleveur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEleveur: 'BBBBBB',
          prenomEleveur: 'BBBBBB',
          telEleveur: 'BBBBBB',
          tel1Eleveur: 'BBBBBB',
          adresse: 'BBBBBB',
          nomElevage: 'BBBBBB',
          descriptionActivite: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Eleveur', () => {
      const patchObject = Object.assign(
        {
          nomEleveur: 'BBBBBB',
          prenomEleveur: 'BBBBBB',
          telEleveur: 'BBBBBB',
          tel1Eleveur: 'BBBBBB',
          adresse: 'BBBBBB',
          nomElevage: 'BBBBBB',
          descriptionActivite: 'BBBBBB',
        },
        new Eleveur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Eleveur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEleveur: 'BBBBBB',
          prenomEleveur: 'BBBBBB',
          telEleveur: 'BBBBBB',
          tel1Eleveur: 'BBBBBB',
          adresse: 'BBBBBB',
          nomElevage: 'BBBBBB',
          descriptionActivite: 'BBBBBB',
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

    it('should delete a Eleveur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEleveurToCollectionIfMissing', () => {
      it('should add a Eleveur to an empty array', () => {
        const eleveur: IEleveur = { id: 123 };
        expectedResult = service.addEleveurToCollectionIfMissing([], eleveur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eleveur);
      });

      it('should not add a Eleveur to an array that contains it', () => {
        const eleveur: IEleveur = { id: 123 };
        const eleveurCollection: IEleveur[] = [
          {
            ...eleveur,
          },
          { id: 456 },
        ];
        expectedResult = service.addEleveurToCollectionIfMissing(eleveurCollection, eleveur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Eleveur to an array that doesn't contain it", () => {
        const eleveur: IEleveur = { id: 123 };
        const eleveurCollection: IEleveur[] = [{ id: 456 }];
        expectedResult = service.addEleveurToCollectionIfMissing(eleveurCollection, eleveur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eleveur);
      });

      it('should add only unique Eleveur to an array', () => {
        const eleveurArray: IEleveur[] = [{ id: 123 }, { id: 456 }, { id: 39439 }];
        const eleveurCollection: IEleveur[] = [{ id: 123 }];
        expectedResult = service.addEleveurToCollectionIfMissing(eleveurCollection, ...eleveurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eleveur: IEleveur = { id: 123 };
        const eleveur2: IEleveur = { id: 456 };
        expectedResult = service.addEleveurToCollectionIfMissing([], eleveur, eleveur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eleveur);
        expect(expectedResult).toContain(eleveur2);
      });

      it('should accept null and undefined values', () => {
        const eleveur: IEleveur = { id: 123 };
        expectedResult = service.addEleveurToCollectionIfMissing([], null, eleveur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eleveur);
      });

      it('should return initial array if no Eleveur is added', () => {
        const eleveurCollection: IEleveur[] = [{ id: 123 }];
        expectedResult = service.addEleveurToCollectionIfMissing(eleveurCollection, undefined, null);
        expect(expectedResult).toEqual(eleveurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
