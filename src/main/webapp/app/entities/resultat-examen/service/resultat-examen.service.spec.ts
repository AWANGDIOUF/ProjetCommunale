import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Examen } from 'app/entities/enumerations/examen.model';
import { IResultatExamen, ResultatExamen } from '../resultat-examen.model';

import { ResultatExamenService } from './resultat-examen.service';

describe('ResultatExamen Service', () => {
  let service: ResultatExamenService;
  let httpMock: HttpTestingController;
  let elemDefault: IResultatExamen;
  let expectedResult: IResultatExamen | IResultatExamen[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResultatExamenService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      typeExament: Examen.CFEE,
      autreExamen: 'AAAAAAA',
      tauxReuissite: 0,
      annee: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ResultatExamen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.create(new ResultatExamen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResultatExamen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeExament: 'BBBBBB',
          autreExamen: 'BBBBBB',
          tauxReuissite: 1,
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResultatExamen', () => {
      const patchObject = Object.assign(
        {
          autreExamen: 'BBBBBB',
        },
        new ResultatExamen()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResultatExamen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeExament: 'BBBBBB',
          autreExamen: 'BBBBBB',
          tauxReuissite: 1,
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ResultatExamen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addResultatExamenToCollectionIfMissing', () => {
      it('should add a ResultatExamen to an empty array', () => {
        const resultatExamen: IResultatExamen = { id: 123 };
        expectedResult = service.addResultatExamenToCollectionIfMissing([], resultatExamen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resultatExamen);
      });

      it('should not add a ResultatExamen to an array that contains it', () => {
        const resultatExamen: IResultatExamen = { id: 123 };
        const resultatExamenCollection: IResultatExamen[] = [
          {
            ...resultatExamen,
          },
          { id: 456 },
        ];
        expectedResult = service.addResultatExamenToCollectionIfMissing(resultatExamenCollection, resultatExamen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResultatExamen to an array that doesn't contain it", () => {
        const resultatExamen: IResultatExamen = { id: 123 };
        const resultatExamenCollection: IResultatExamen[] = [{ id: 456 }];
        expectedResult = service.addResultatExamenToCollectionIfMissing(resultatExamenCollection, resultatExamen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resultatExamen);
      });

      it('should add only unique ResultatExamen to an array', () => {
        const resultatExamenArray: IResultatExamen[] = [{ id: 123 }, { id: 456 }, { id: 672 }];
        const resultatExamenCollection: IResultatExamen[] = [{ id: 123 }];
        expectedResult = service.addResultatExamenToCollectionIfMissing(resultatExamenCollection, ...resultatExamenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resultatExamen: IResultatExamen = { id: 123 };
        const resultatExamen2: IResultatExamen = { id: 456 };
        expectedResult = service.addResultatExamenToCollectionIfMissing([], resultatExamen, resultatExamen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resultatExamen);
        expect(expectedResult).toContain(resultatExamen2);
      });

      it('should accept null and undefined values', () => {
        const resultatExamen: IResultatExamen = { id: 123 };
        expectedResult = service.addResultatExamenToCollectionIfMissing([], null, resultatExamen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resultatExamen);
      });

      it('should return initial array if no ResultatExamen is added', () => {
        const resultatExamenCollection: IResultatExamen[] = [{ id: 123 }];
        expectedResult = service.addResultatExamenToCollectionIfMissing(resultatExamenCollection, undefined, null);
        expect(expectedResult).toEqual(resultatExamenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
