import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sport } from 'app/entities/enumerations/sport.model';
import { ITypeSport, TypeSport } from '../type-sport.model';

import { TypeSportService } from './type-sport.service';

describe('Service Tests', () => {
  describe('TypeSport Service', () => {
    let service: TypeSportService;
    let httpMock: HttpTestingController;
    let elemDefault: ITypeSport;
    let expectedResult: ITypeSport | ITypeSport[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeSportService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        sport: Sport.FOOTBALL,
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

      it('should create a TypeSport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TypeSport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TypeSport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sport: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TypeSport', () => {
        const patchObject = Object.assign(
          {
            sport: 'BBBBBB',
          },
          new TypeSport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TypeSport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sport: 'BBBBBB',
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

      it('should delete a TypeSport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeSportToCollectionIfMissing', () => {
        it('should add a TypeSport to an empty array', () => {
          const typeSport: ITypeSport = { id: 123 };
          expectedResult = service.addTypeSportToCollectionIfMissing([], typeSport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeSport);
        });

        it('should not add a TypeSport to an array that contains it', () => {
          const typeSport: ITypeSport = { id: 123 };
          const typeSportCollection: ITypeSport[] = [
            {
              ...typeSport,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeSportToCollectionIfMissing(typeSportCollection, typeSport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TypeSport to an array that doesn't contain it", () => {
          const typeSport: ITypeSport = { id: 123 };
          const typeSportCollection: ITypeSport[] = [{ id: 456 }];
          expectedResult = service.addTypeSportToCollectionIfMissing(typeSportCollection, typeSport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeSport);
        });

        it('should add only unique TypeSport to an array', () => {
          const typeSportArray: ITypeSport[] = [{ id: 123 }, { id: 456 }, { id: 14446 }];
          const typeSportCollection: ITypeSport[] = [{ id: 123 }];
          expectedResult = service.addTypeSportToCollectionIfMissing(typeSportCollection, ...typeSportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const typeSport: ITypeSport = { id: 123 };
          const typeSport2: ITypeSport = { id: 456 };
          expectedResult = service.addTypeSportToCollectionIfMissing([], typeSport, typeSport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeSport);
          expect(expectedResult).toContain(typeSport2);
        });

        it('should accept null and undefined values', () => {
          const typeSport: ITypeSport = { id: 123 };
          expectedResult = service.addTypeSportToCollectionIfMissing([], null, typeSport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeSport);
        });

        it('should return initial array if no TypeSport is added', () => {
          const typeSportCollection: ITypeSport[] = [{ id: 123 }];
          expectedResult = service.addTypeSportToCollectionIfMissing(typeSportCollection, undefined, null);
          expect(expectedResult).toEqual(typeSportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
