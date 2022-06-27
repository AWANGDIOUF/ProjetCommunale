import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICalendrierEvenement, CalendrierEvenement } from '../calendrier-evenement.model';

import { CalendrierEvenementService } from './calendrier-evenement.service';

describe('CalendrierEvenement Service', () => {
  let service: CalendrierEvenementService;
  let httpMock: HttpTestingController;
  let elemDefault: ICalendrierEvenement;
  let expectedResult: ICalendrierEvenement | ICalendrierEvenement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CalendrierEvenementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomEve: 'AAAAAAA',
      but: 'AAAAAAA',
      objectif: 'AAAAAAA',
      date: currentDate,
      lieu: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CalendrierEvenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new CalendrierEvenement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CalendrierEvenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEve: 'BBBBBB',
          but: 'BBBBBB',
          objectif: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          lieu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CalendrierEvenement', () => {
      const patchObject = Object.assign(
        {
          nomEve: 'BBBBBB',
          but: 'BBBBBB',
        },
        new CalendrierEvenement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CalendrierEvenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEve: 'BBBBBB',
          but: 'BBBBBB',
          objectif: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          lieu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CalendrierEvenement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCalendrierEvenementToCollectionIfMissing', () => {
      it('should add a CalendrierEvenement to an empty array', () => {
        const calendrierEvenement: ICalendrierEvenement = { id: 123 };
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing([], calendrierEvenement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(calendrierEvenement);
      });

      it('should not add a CalendrierEvenement to an array that contains it', () => {
        const calendrierEvenement: ICalendrierEvenement = { id: 123 };
        const calendrierEvenementCollection: ICalendrierEvenement[] = [
          {
            ...calendrierEvenement,
          },
          { id: 456 },
        ];
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing(calendrierEvenementCollection, calendrierEvenement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CalendrierEvenement to an array that doesn't contain it", () => {
        const calendrierEvenement: ICalendrierEvenement = { id: 123 };
        const calendrierEvenementCollection: ICalendrierEvenement[] = [{ id: 456 }];
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing(calendrierEvenementCollection, calendrierEvenement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calendrierEvenement);
      });

      it('should add only unique CalendrierEvenement to an array', () => {
        const calendrierEvenementArray: ICalendrierEvenement[] = [{ id: 123 }, { id: 456 }, { id: 12978 }];
        const calendrierEvenementCollection: ICalendrierEvenement[] = [{ id: 123 }];
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing(calendrierEvenementCollection, ...calendrierEvenementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const calendrierEvenement: ICalendrierEvenement = { id: 123 };
        const calendrierEvenement2: ICalendrierEvenement = { id: 456 };
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing([], calendrierEvenement, calendrierEvenement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calendrierEvenement);
        expect(expectedResult).toContain(calendrierEvenement2);
      });

      it('should accept null and undefined values', () => {
        const calendrierEvenement: ICalendrierEvenement = { id: 123 };
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing([], null, calendrierEvenement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(calendrierEvenement);
      });

      it('should return initial array if no CalendrierEvenement is added', () => {
        const calendrierEvenementCollection: ICalendrierEvenement[] = [{ id: 123 }];
        expectedResult = service.addCalendrierEvenementToCollectionIfMissing(calendrierEvenementCollection, undefined, null);
        expect(expectedResult).toEqual(calendrierEvenementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
