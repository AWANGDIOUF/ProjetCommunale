import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CibleVacc } from 'app/entities/enumerations/cible-vacc.model';
import { ICible, Cible } from '../cible.model';

import { CibleService } from './cible.service';

describe('Service Tests', () => {
  describe('Cible Service', () => {
    let service: CibleService;
    let httpMock: HttpTestingController;
    let elemDefault: ICible;
    let expectedResult: ICible | ICible[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CibleService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        cible: CibleVacc.Enfant,
        age: 0,
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

      it('should create a Cible', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Cible()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Cible', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cible: 'BBBBBB',
            age: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Cible', () => {
        const patchObject = Object.assign({}, new Cible());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Cible', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cible: 'BBBBBB',
            age: 1,
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

      it('should delete a Cible', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCibleToCollectionIfMissing', () => {
        it('should add a Cible to an empty array', () => {
          const cible: ICible = { id: 123 };
          expectedResult = service.addCibleToCollectionIfMissing([], cible);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cible);
        });

        it('should not add a Cible to an array that contains it', () => {
          const cible: ICible = { id: 123 };
          const cibleCollection: ICible[] = [
            {
              ...cible,
            },
            { id: 456 },
          ];
          expectedResult = service.addCibleToCollectionIfMissing(cibleCollection, cible);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Cible to an array that doesn't contain it", () => {
          const cible: ICible = { id: 123 };
          const cibleCollection: ICible[] = [{ id: 456 }];
          expectedResult = service.addCibleToCollectionIfMissing(cibleCollection, cible);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cible);
        });

        it('should add only unique Cible to an array', () => {
          const cibleArray: ICible[] = [{ id: 123 }, { id: 456 }, { id: 81131 }];
          const cibleCollection: ICible[] = [{ id: 123 }];
          expectedResult = service.addCibleToCollectionIfMissing(cibleCollection, ...cibleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cible: ICible = { id: 123 };
          const cible2: ICible = { id: 456 };
          expectedResult = service.addCibleToCollectionIfMissing([], cible, cible2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cible);
          expect(expectedResult).toContain(cible2);
        });

        it('should accept null and undefined values', () => {
          const cible: ICible = { id: 123 };
          expectedResult = service.addCibleToCollectionIfMissing([], null, cible, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cible);
        });

        it('should return initial array if no Cible is added', () => {
          const cibleCollection: ICible[] = [{ id: 123 }];
          expectedResult = service.addCibleToCollectionIfMissing(cibleCollection, undefined, null);
          expect(expectedResult).toEqual(cibleCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
