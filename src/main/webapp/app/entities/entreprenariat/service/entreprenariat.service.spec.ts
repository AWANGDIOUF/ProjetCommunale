import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeEntreprenariat } from 'app/entities/enumerations/type-entreprenariat.model';
import { IEntreprenariat, Entreprenariat } from '../entreprenariat.model';

import { EntreprenariatService } from './entreprenariat.service';

describe('Entreprenariat Service', () => {
  let service: EntreprenariatService;
  let httpMock: HttpTestingController;
  let elemDefault: IEntreprenariat;
  let expectedResult: IEntreprenariat | IEntreprenariat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EntreprenariatService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      typeEntre: TypeEntreprenariat.Social,
      autreEntre: 'AAAAAAA',
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

    it('should create a Entreprenariat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Entreprenariat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Entreprenariat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeEntre: 'BBBBBB',
          autreEntre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Entreprenariat', () => {
      const patchObject = Object.assign(
        {
          typeEntre: 'BBBBBB',
        },
        new Entreprenariat()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Entreprenariat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeEntre: 'BBBBBB',
          autreEntre: 'BBBBBB',
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

    it('should delete a Entreprenariat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEntreprenariatToCollectionIfMissing', () => {
      it('should add a Entreprenariat to an empty array', () => {
        const entreprenariat: IEntreprenariat = { id: 123 };
        expectedResult = service.addEntreprenariatToCollectionIfMissing([], entreprenariat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entreprenariat);
      });

      it('should not add a Entreprenariat to an array that contains it', () => {
        const entreprenariat: IEntreprenariat = { id: 123 };
        const entreprenariatCollection: IEntreprenariat[] = [
          {
            ...entreprenariat,
          },
          { id: 456 },
        ];
        expectedResult = service.addEntreprenariatToCollectionIfMissing(entreprenariatCollection, entreprenariat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Entreprenariat to an array that doesn't contain it", () => {
        const entreprenariat: IEntreprenariat = { id: 123 };
        const entreprenariatCollection: IEntreprenariat[] = [{ id: 456 }];
        expectedResult = service.addEntreprenariatToCollectionIfMissing(entreprenariatCollection, entreprenariat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entreprenariat);
      });

      it('should add only unique Entreprenariat to an array', () => {
        const entreprenariatArray: IEntreprenariat[] = [{ id: 123 }, { id: 456 }, { id: 49802 }];
        const entreprenariatCollection: IEntreprenariat[] = [{ id: 123 }];
        expectedResult = service.addEntreprenariatToCollectionIfMissing(entreprenariatCollection, ...entreprenariatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entreprenariat: IEntreprenariat = { id: 123 };
        const entreprenariat2: IEntreprenariat = { id: 456 };
        expectedResult = service.addEntreprenariatToCollectionIfMissing([], entreprenariat, entreprenariat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entreprenariat);
        expect(expectedResult).toContain(entreprenariat2);
      });

      it('should accept null and undefined values', () => {
        const entreprenariat: IEntreprenariat = { id: 123 };
        expectedResult = service.addEntreprenariatToCollectionIfMissing([], null, entreprenariat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entreprenariat);
      });

      it('should return initial array if no Entreprenariat is added', () => {
        const entreprenariatCollection: IEntreprenariat[] = [{ id: 123 }];
        expectedResult = service.addEntreprenariatToCollectionIfMissing(entreprenariatCollection, undefined, null);
        expect(expectedResult).toEqual(entreprenariatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
