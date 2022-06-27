import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvenement, Evenement } from '../evenement.model';

import { EvenementService } from './evenement.service';

describe('Evenement Service', () => {
  let service: EvenementService;
  let httpMock: HttpTestingController;
  let elemDefault: IEvenement;
  let expectedResult: IEvenement | IEvenement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EvenementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomEvenement: 'AAAAAAA',
      libelle: 'AAAAAAA',
      action: 'AAAAAAA',
      decision: 'AAAAAAA',
      delaiInstruction: currentDate,
      delaiInscription: currentDate,
      delaiValidation: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          delaiInstruction: currentDate.format(DATE_TIME_FORMAT),
          delaiInscription: currentDate.format(DATE_TIME_FORMAT),
          delaiValidation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Evenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          delaiInstruction: currentDate.format(DATE_TIME_FORMAT),
          delaiInscription: currentDate.format(DATE_TIME_FORMAT),
          delaiValidation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          delaiInstruction: currentDate,
          delaiInscription: currentDate,
          delaiValidation: currentDate,
        },
        returnedFromService
      );

      service.create(new Evenement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEvenement: 'BBBBBB',
          libelle: 'BBBBBB',
          action: 'BBBBBB',
          decision: 'BBBBBB',
          delaiInstruction: currentDate.format(DATE_TIME_FORMAT),
          delaiInscription: currentDate.format(DATE_TIME_FORMAT),
          delaiValidation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          delaiInstruction: currentDate,
          delaiInscription: currentDate,
          delaiValidation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evenement', () => {
      const patchObject = Object.assign(
        {
          libelle: 'BBBBBB',
          action: 'BBBBBB',
          decision: 'BBBBBB',
          delaiInscription: currentDate.format(DATE_TIME_FORMAT),
          delaiValidation: currentDate.format(DATE_TIME_FORMAT),
        },
        new Evenement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          delaiInstruction: currentDate,
          delaiInscription: currentDate,
          delaiValidation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evenement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEvenement: 'BBBBBB',
          libelle: 'BBBBBB',
          action: 'BBBBBB',
          decision: 'BBBBBB',
          delaiInstruction: currentDate.format(DATE_TIME_FORMAT),
          delaiInscription: currentDate.format(DATE_TIME_FORMAT),
          delaiValidation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          delaiInstruction: currentDate,
          delaiInscription: currentDate,
          delaiValidation: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Evenement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEvenementToCollectionIfMissing', () => {
      it('should add a Evenement to an empty array', () => {
        const evenement: IEvenement = { id: 123 };
        expectedResult = service.addEvenementToCollectionIfMissing([], evenement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evenement);
      });

      it('should not add a Evenement to an array that contains it', () => {
        const evenement: IEvenement = { id: 123 };
        const evenementCollection: IEvenement[] = [
          {
            ...evenement,
          },
          { id: 456 },
        ];
        expectedResult = service.addEvenementToCollectionIfMissing(evenementCollection, evenement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evenement to an array that doesn't contain it", () => {
        const evenement: IEvenement = { id: 123 };
        const evenementCollection: IEvenement[] = [{ id: 456 }];
        expectedResult = service.addEvenementToCollectionIfMissing(evenementCollection, evenement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evenement);
      });

      it('should add only unique Evenement to an array', () => {
        const evenementArray: IEvenement[] = [{ id: 123 }, { id: 456 }, { id: 84760 }];
        const evenementCollection: IEvenement[] = [{ id: 123 }];
        expectedResult = service.addEvenementToCollectionIfMissing(evenementCollection, ...evenementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evenement: IEvenement = { id: 123 };
        const evenement2: IEvenement = { id: 456 };
        expectedResult = service.addEvenementToCollectionIfMissing([], evenement, evenement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evenement);
        expect(expectedResult).toContain(evenement2);
      });

      it('should accept null and undefined values', () => {
        const evenement: IEvenement = { id: 123 };
        expectedResult = service.addEvenementToCollectionIfMissing([], null, evenement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evenement);
      });

      it('should return initial array if no Evenement is added', () => {
        const evenementCollection: IEvenement[] = [{ id: 123 }];
        expectedResult = service.addEvenementToCollectionIfMissing(evenementCollection, undefined, null);
        expect(expectedResult).toEqual(evenementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
