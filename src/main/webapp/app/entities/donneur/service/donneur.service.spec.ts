import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeDonneur } from 'app/entities/enumerations/type-donneur.model';
import { IDonneur, Donneur } from '../donneur.model';

import { DonneurService } from './donneur.service';

describe('Donneur Service', () => {
  let service: DonneurService;
  let httpMock: HttpTestingController;
  let elemDefault: IDonneur;
  let expectedResult: IDonneur | IDonneur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DonneurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      typeDonneur: TypeDonneur.ONG,
      prenom: 'AAAAAAA',
      nom: 'AAAAAAA',
      email: 'AAAAAAA',
      adresse: 'AAAAAAA',
      tel1: 'AAAAAAA',
      ville: 'AAAAAAA',
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

    it('should create a Donneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Donneur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Donneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeDonneur: 'BBBBBB',
          prenom: 'BBBBBB',
          nom: 'BBBBBB',
          email: 'BBBBBB',
          adresse: 'BBBBBB',
          tel1: 'BBBBBB',
          ville: 'BBBBBB',
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

    it('should partial update a Donneur', () => {
      const patchObject = Object.assign(
        {
          typeDonneur: 'BBBBBB',
          prenom: 'BBBBBB',
          nom: 'BBBBBB',
          email: 'BBBBBB',
        },
        new Donneur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Donneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeDonneur: 'BBBBBB',
          prenom: 'BBBBBB',
          nom: 'BBBBBB',
          email: 'BBBBBB',
          adresse: 'BBBBBB',
          tel1: 'BBBBBB',
          ville: 'BBBBBB',
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

    it('should delete a Donneur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDonneurToCollectionIfMissing', () => {
      it('should add a Donneur to an empty array', () => {
        const donneur: IDonneur = { id: 123 };
        expectedResult = service.addDonneurToCollectionIfMissing([], donneur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donneur);
      });

      it('should not add a Donneur to an array that contains it', () => {
        const donneur: IDonneur = { id: 123 };
        const donneurCollection: IDonneur[] = [
          {
            ...donneur,
          },
          { id: 456 },
        ];
        expectedResult = service.addDonneurToCollectionIfMissing(donneurCollection, donneur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Donneur to an array that doesn't contain it", () => {
        const donneur: IDonneur = { id: 123 };
        const donneurCollection: IDonneur[] = [{ id: 456 }];
        expectedResult = service.addDonneurToCollectionIfMissing(donneurCollection, donneur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donneur);
      });

      it('should add only unique Donneur to an array', () => {
        const donneurArray: IDonneur[] = [{ id: 123 }, { id: 456 }, { id: 76880 }];
        const donneurCollection: IDonneur[] = [{ id: 123 }];
        expectedResult = service.addDonneurToCollectionIfMissing(donneurCollection, ...donneurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const donneur: IDonneur = { id: 123 };
        const donneur2: IDonneur = { id: 456 };
        expectedResult = service.addDonneurToCollectionIfMissing([], donneur, donneur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donneur);
        expect(expectedResult).toContain(donneur2);
      });

      it('should accept null and undefined values', () => {
        const donneur: IDonneur = { id: 123 };
        expectedResult = service.addDonneurToCollectionIfMissing([], null, donneur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donneur);
      });

      it('should return initial array if no Donneur is added', () => {
        const donneurCollection: IDonneur[] = [{ id: 123 }];
        expectedResult = service.addDonneurToCollectionIfMissing(donneurCollection, undefined, null);
        expect(expectedResult).toEqual(donneurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
