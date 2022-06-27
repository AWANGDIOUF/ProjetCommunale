import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';

import { TypeVaccinService } from './type-vaccin.service';

describe('TypeVaccin Service', () => {
  let service: TypeVaccinService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypeVaccin;
  let expectedResult: ITypeVaccin | ITypeVaccin[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeVaccinService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
      objectif: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          objectif: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TypeVaccin', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          objectif: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          objectif: currentDate,
        },
        returnedFromService
      );

      service.create(new TypeVaccin()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeVaccin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          objectif: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          objectif: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeVaccin', () => {
      const patchObject = Object.assign({}, new TypeVaccin());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          objectif: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeVaccin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          objectif: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          objectif: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TypeVaccin', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypeVaccinToCollectionIfMissing', () => {
      it('should add a TypeVaccin to an empty array', () => {
        const typeVaccin: ITypeVaccin = { id: 123 };
        expectedResult = service.addTypeVaccinToCollectionIfMissing([], typeVaccin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeVaccin);
      });

      it('should not add a TypeVaccin to an array that contains it', () => {
        const typeVaccin: ITypeVaccin = { id: 123 };
        const typeVaccinCollection: ITypeVaccin[] = [
          {
            ...typeVaccin,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypeVaccinToCollectionIfMissing(typeVaccinCollection, typeVaccin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeVaccin to an array that doesn't contain it", () => {
        const typeVaccin: ITypeVaccin = { id: 123 };
        const typeVaccinCollection: ITypeVaccin[] = [{ id: 456 }];
        expectedResult = service.addTypeVaccinToCollectionIfMissing(typeVaccinCollection, typeVaccin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeVaccin);
      });

      it('should add only unique TypeVaccin to an array', () => {
        const typeVaccinArray: ITypeVaccin[] = [{ id: 123 }, { id: 456 }, { id: 72985 }];
        const typeVaccinCollection: ITypeVaccin[] = [{ id: 123 }];
        expectedResult = service.addTypeVaccinToCollectionIfMissing(typeVaccinCollection, ...typeVaccinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeVaccin: ITypeVaccin = { id: 123 };
        const typeVaccin2: ITypeVaccin = { id: 456 };
        expectedResult = service.addTypeVaccinToCollectionIfMissing([], typeVaccin, typeVaccin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeVaccin);
        expect(expectedResult).toContain(typeVaccin2);
      });

      it('should accept null and undefined values', () => {
        const typeVaccin: ITypeVaccin = { id: 123 };
        expectedResult = service.addTypeVaccinToCollectionIfMissing([], null, typeVaccin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeVaccin);
      });

      it('should return initial array if no TypeVaccin is added', () => {
        const typeVaccinCollection: ITypeVaccin[] = [{ id: 123 }];
        expectedResult = service.addTypeVaccinToCollectionIfMissing(typeVaccinCollection, undefined, null);
        expect(expectedResult).toEqual(typeVaccinCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
