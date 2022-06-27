import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnsegnant, Ensegnant } from '../ensegnant.model';

import { EnsegnantService } from './ensegnant.service';

describe('Ensegnant Service', () => {
  let service: EnsegnantService;
  let httpMock: HttpTestingController;
  let elemDefault: IEnsegnant;
  let expectedResult: IEnsegnant | IEnsegnant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EnsegnantService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      email: 'AAAAAAA',
      tel: 'AAAAAAA',
      tel1: 'AAAAAAA',
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

    it('should create a Ensegnant', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ensegnant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ensegnant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          tel: 'BBBBBB',
          tel1: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ensegnant', () => {
      const patchObject = Object.assign(
        {
          email: 'BBBBBB',
          tel: 'BBBBBB',
        },
        new Ensegnant()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ensegnant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          tel: 'BBBBBB',
          tel1: 'BBBBBB',
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

    it('should delete a Ensegnant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEnsegnantToCollectionIfMissing', () => {
      it('should add a Ensegnant to an empty array', () => {
        const ensegnant: IEnsegnant = { id: 123 };
        expectedResult = service.addEnsegnantToCollectionIfMissing([], ensegnant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ensegnant);
      });

      it('should not add a Ensegnant to an array that contains it', () => {
        const ensegnant: IEnsegnant = { id: 123 };
        const ensegnantCollection: IEnsegnant[] = [
          {
            ...ensegnant,
          },
          { id: 456 },
        ];
        expectedResult = service.addEnsegnantToCollectionIfMissing(ensegnantCollection, ensegnant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ensegnant to an array that doesn't contain it", () => {
        const ensegnant: IEnsegnant = { id: 123 };
        const ensegnantCollection: IEnsegnant[] = [{ id: 456 }];
        expectedResult = service.addEnsegnantToCollectionIfMissing(ensegnantCollection, ensegnant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ensegnant);
      });

      it('should add only unique Ensegnant to an array', () => {
        const ensegnantArray: IEnsegnant[] = [{ id: 123 }, { id: 456 }, { id: 29683 }];
        const ensegnantCollection: IEnsegnant[] = [{ id: 123 }];
        expectedResult = service.addEnsegnantToCollectionIfMissing(ensegnantCollection, ...ensegnantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ensegnant: IEnsegnant = { id: 123 };
        const ensegnant2: IEnsegnant = { id: 456 };
        expectedResult = service.addEnsegnantToCollectionIfMissing([], ensegnant, ensegnant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ensegnant);
        expect(expectedResult).toContain(ensegnant2);
      });

      it('should accept null and undefined values', () => {
        const ensegnant: IEnsegnant = { id: 123 };
        expectedResult = service.addEnsegnantToCollectionIfMissing([], null, ensegnant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ensegnant);
      });

      it('should return initial array if no Ensegnant is added', () => {
        const ensegnantCollection: IEnsegnant[] = [{ id: 123 }];
        expectedResult = service.addEnsegnantToCollectionIfMissing(ensegnantCollection, undefined, null);
        expect(expectedResult).toEqual(ensegnantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
