import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICombattant, Combattant } from '../combattant.model';

import { CombattantService } from './combattant.service';

describe('Combattant Service', () => {
  let service: CombattantService;
  let httpMock: HttpTestingController;
  let elemDefault: ICombattant;
  let expectedResult: ICombattant | ICombattant[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CombattantService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomCombattant: 'AAAAAAA',
      prenomCombattant: 'AAAAAAA',
      dateNaisCombattant: currentDate,
      lieuNaisCombattant: 'AAAAAAA',
      photoCombattantContentType: 'image/png',
      photoCombattant: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateNaisCombattant: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Combattant', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateNaisCombattant: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisCombattant: currentDate,
        },
        returnedFromService
      );

      service.create(new Combattant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Combattant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCombattant: 'BBBBBB',
          prenomCombattant: 'BBBBBB',
          dateNaisCombattant: currentDate.format(DATE_FORMAT),
          lieuNaisCombattant: 'BBBBBB',
          photoCombattant: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisCombattant: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Combattant', () => {
      const patchObject = Object.assign(
        {
          nomCombattant: 'BBBBBB',
          prenomCombattant: 'BBBBBB',
          dateNaisCombattant: currentDate.format(DATE_FORMAT),
          lieuNaisCombattant: 'BBBBBB',
          photoCombattant: 'BBBBBB',
        },
        new Combattant()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateNaisCombattant: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Combattant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCombattant: 'BBBBBB',
          prenomCombattant: 'BBBBBB',
          dateNaisCombattant: currentDate.format(DATE_FORMAT),
          lieuNaisCombattant: 'BBBBBB',
          photoCombattant: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaisCombattant: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Combattant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCombattantToCollectionIfMissing', () => {
      it('should add a Combattant to an empty array', () => {
        const combattant: ICombattant = { id: 123 };
        expectedResult = service.addCombattantToCollectionIfMissing([], combattant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(combattant);
      });

      it('should not add a Combattant to an array that contains it', () => {
        const combattant: ICombattant = { id: 123 };
        const combattantCollection: ICombattant[] = [
          {
            ...combattant,
          },
          { id: 456 },
        ];
        expectedResult = service.addCombattantToCollectionIfMissing(combattantCollection, combattant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Combattant to an array that doesn't contain it", () => {
        const combattant: ICombattant = { id: 123 };
        const combattantCollection: ICombattant[] = [{ id: 456 }];
        expectedResult = service.addCombattantToCollectionIfMissing(combattantCollection, combattant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(combattant);
      });

      it('should add only unique Combattant to an array', () => {
        const combattantArray: ICombattant[] = [{ id: 123 }, { id: 456 }, { id: 69282 }];
        const combattantCollection: ICombattant[] = [{ id: 123 }];
        expectedResult = service.addCombattantToCollectionIfMissing(combattantCollection, ...combattantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const combattant: ICombattant = { id: 123 };
        const combattant2: ICombattant = { id: 456 };
        expectedResult = service.addCombattantToCollectionIfMissing([], combattant, combattant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(combattant);
        expect(expectedResult).toContain(combattant2);
      });

      it('should accept null and undefined values', () => {
        const combattant: ICombattant = { id: 123 };
        expectedResult = service.addCombattantToCollectionIfMissing([], null, combattant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(combattant);
      });

      it('should return initial array if no Combattant is added', () => {
        const combattantCollection: ICombattant[] = [{ id: 123 }];
        expectedResult = service.addCombattantToCollectionIfMissing(combattantCollection, undefined, null);
        expect(expectedResult).toEqual(combattantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
