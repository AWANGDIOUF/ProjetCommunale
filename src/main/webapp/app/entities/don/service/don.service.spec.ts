import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeDon } from 'app/entities/enumerations/type-don.model';
import { IDon, Don } from '../don.model';

import { DonService } from './don.service';

describe('Don Service', () => {
  let service: DonService;
  let httpMock: HttpTestingController;
  let elemDefault: IDon;
  let expectedResult: IDon | IDon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DonService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      typeDon: TypeDon.Ravitament,
      montant: 0,
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

    it('should create a Don', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Don()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Don', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeDon: 'BBBBBB',
          montant: 1,
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

    it('should partial update a Don', () => {
      const patchObject = Object.assign(
        {
          typeDon: 'BBBBBB',
        },
        new Don()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Don', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeDon: 'BBBBBB',
          montant: 1,
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

    it('should delete a Don', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDonToCollectionIfMissing', () => {
      it('should add a Don to an empty array', () => {
        const don: IDon = { id: 123 };
        expectedResult = service.addDonToCollectionIfMissing([], don);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(don);
      });

      it('should not add a Don to an array that contains it', () => {
        const don: IDon = { id: 123 };
        const donCollection: IDon[] = [
          {
            ...don,
          },
          { id: 456 },
        ];
        expectedResult = service.addDonToCollectionIfMissing(donCollection, don);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Don to an array that doesn't contain it", () => {
        const don: IDon = { id: 123 };
        const donCollection: IDon[] = [{ id: 456 }];
        expectedResult = service.addDonToCollectionIfMissing(donCollection, don);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(don);
      });

      it('should add only unique Don to an array', () => {
        const donArray: IDon[] = [{ id: 123 }, { id: 456 }, { id: 43096 }];
        const donCollection: IDon[] = [{ id: 123 }];
        expectedResult = service.addDonToCollectionIfMissing(donCollection, ...donArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const don: IDon = { id: 123 };
        const don2: IDon = { id: 456 };
        expectedResult = service.addDonToCollectionIfMissing([], don, don2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(don);
        expect(expectedResult).toContain(don2);
      });

      it('should accept null and undefined values', () => {
        const don: IDon = { id: 123 };
        expectedResult = service.addDonToCollectionIfMissing([], null, don, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(don);
      });

      it('should return initial array if no Don is added', () => {
        const donCollection: IDon[] = [{ id: 123 }];
        expectedResult = service.addDonToCollectionIfMissing(donCollection, undefined, null);
        expect(expectedResult).toEqual(donCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
