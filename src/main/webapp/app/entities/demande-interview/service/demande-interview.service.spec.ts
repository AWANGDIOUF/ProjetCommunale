import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDemandeInterview, DemandeInterview } from '../demande-interview.model';

import { DemandeInterviewService } from './demande-interview.service';

describe('DemandeInterview Service', () => {
  let service: DemandeInterviewService;
  let httpMock: HttpTestingController;
  let elemDefault: IDemandeInterview;
  let expectedResult: IDemandeInterview | IDemandeInterview[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeInterviewService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomJournaliste: 'AAAAAAA',
      prenomJournaliste: 'AAAAAAA',
      nomSociete: 'AAAAAAA',
      emailJournalite: 'AAAAAAA',
      dateInterview: currentDate,
      etatDemande: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateInterview: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DemandeInterview', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateInterview: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateInterview: currentDate,
        },
        returnedFromService
      );

      service.create(new DemandeInterview()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeInterview', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomJournaliste: 'BBBBBB',
          prenomJournaliste: 'BBBBBB',
          nomSociete: 'BBBBBB',
          emailJournalite: 'BBBBBB',
          dateInterview: currentDate.format(DATE_FORMAT),
          etatDemande: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateInterview: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemandeInterview', () => {
      const patchObject = Object.assign(
        {
          nomJournaliste: 'BBBBBB',
          nomSociete: 'BBBBBB',
        },
        new DemandeInterview()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateInterview: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemandeInterview', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomJournaliste: 'BBBBBB',
          prenomJournaliste: 'BBBBBB',
          nomSociete: 'BBBBBB',
          emailJournalite: 'BBBBBB',
          dateInterview: currentDate.format(DATE_FORMAT),
          etatDemande: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateInterview: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DemandeInterview', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDemandeInterviewToCollectionIfMissing', () => {
      it('should add a DemandeInterview to an empty array', () => {
        const demandeInterview: IDemandeInterview = { id: 123 };
        expectedResult = service.addDemandeInterviewToCollectionIfMissing([], demandeInterview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeInterview);
      });

      it('should not add a DemandeInterview to an array that contains it', () => {
        const demandeInterview: IDemandeInterview = { id: 123 };
        const demandeInterviewCollection: IDemandeInterview[] = [
          {
            ...demandeInterview,
          },
          { id: 456 },
        ];
        expectedResult = service.addDemandeInterviewToCollectionIfMissing(demandeInterviewCollection, demandeInterview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeInterview to an array that doesn't contain it", () => {
        const demandeInterview: IDemandeInterview = { id: 123 };
        const demandeInterviewCollection: IDemandeInterview[] = [{ id: 456 }];
        expectedResult = service.addDemandeInterviewToCollectionIfMissing(demandeInterviewCollection, demandeInterview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeInterview);
      });

      it('should add only unique DemandeInterview to an array', () => {
        const demandeInterviewArray: IDemandeInterview[] = [{ id: 123 }, { id: 456 }, { id: 1623 }];
        const demandeInterviewCollection: IDemandeInterview[] = [{ id: 123 }];
        expectedResult = service.addDemandeInterviewToCollectionIfMissing(demandeInterviewCollection, ...demandeInterviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeInterview: IDemandeInterview = { id: 123 };
        const demandeInterview2: IDemandeInterview = { id: 456 };
        expectedResult = service.addDemandeInterviewToCollectionIfMissing([], demandeInterview, demandeInterview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeInterview);
        expect(expectedResult).toContain(demandeInterview2);
      });

      it('should accept null and undefined values', () => {
        const demandeInterview: IDemandeInterview = { id: 123 };
        expectedResult = service.addDemandeInterviewToCollectionIfMissing([], null, demandeInterview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeInterview);
      });

      it('should return initial array if no DemandeInterview is added', () => {
        const demandeInterviewCollection: IDemandeInterview[] = [{ id: 123 }];
        expectedResult = service.addDemandeInterviewToCollectionIfMissing(demandeInterviewCollection, undefined, null);
        expect(expectedResult).toEqual(demandeInterviewCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
