import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISensibiisationInternet, SensibiisationInternet } from '../sensibiisation-internet.model';

import { SensibiisationInternetService } from './sensibiisation-internet.service';

describe('SensibiisationInternet Service', () => {
  let service: SensibiisationInternetService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensibiisationInternet;
  let expectedResult: ISensibiisationInternet | ISensibiisationInternet[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensibiisationInternetService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      theme: currentDate,
      interdiction: 'AAAAAAA',
      bonnePratique: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          theme: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SensibiisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          theme: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          theme: currentDate,
        },
        returnedFromService
      );

      service.create(new SensibiisationInternet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SensibiisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          theme: currentDate.format(DATE_FORMAT),
          interdiction: 'BBBBBB',
          bonnePratique: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          theme: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SensibiisationInternet', () => {
      const patchObject = Object.assign(
        {
          theme: currentDate.format(DATE_FORMAT),
          interdiction: 'BBBBBB',
          bonnePratique: 'BBBBBB',
        },
        new SensibiisationInternet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          theme: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SensibiisationInternet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          theme: currentDate.format(DATE_FORMAT),
          interdiction: 'BBBBBB',
          bonnePratique: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          theme: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SensibiisationInternet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensibiisationInternetToCollectionIfMissing', () => {
      it('should add a SensibiisationInternet to an empty array', () => {
        const sensibiisationInternet: ISensibiisationInternet = { id: 123 };
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing([], sensibiisationInternet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensibiisationInternet);
      });

      it('should not add a SensibiisationInternet to an array that contains it', () => {
        const sensibiisationInternet: ISensibiisationInternet = { id: 123 };
        const sensibiisationInternetCollection: ISensibiisationInternet[] = [
          {
            ...sensibiisationInternet,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing(sensibiisationInternetCollection, sensibiisationInternet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SensibiisationInternet to an array that doesn't contain it", () => {
        const sensibiisationInternet: ISensibiisationInternet = { id: 123 };
        const sensibiisationInternetCollection: ISensibiisationInternet[] = [{ id: 456 }];
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing(sensibiisationInternetCollection, sensibiisationInternet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensibiisationInternet);
      });

      it('should add only unique SensibiisationInternet to an array', () => {
        const sensibiisationInternetArray: ISensibiisationInternet[] = [{ id: 123 }, { id: 456 }, { id: 69521 }];
        const sensibiisationInternetCollection: ISensibiisationInternet[] = [{ id: 123 }];
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing(
          sensibiisationInternetCollection,
          ...sensibiisationInternetArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensibiisationInternet: ISensibiisationInternet = { id: 123 };
        const sensibiisationInternet2: ISensibiisationInternet = { id: 456 };
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing([], sensibiisationInternet, sensibiisationInternet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensibiisationInternet);
        expect(expectedResult).toContain(sensibiisationInternet2);
      });

      it('should accept null and undefined values', () => {
        const sensibiisationInternet: ISensibiisationInternet = { id: 123 };
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing([], null, sensibiisationInternet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensibiisationInternet);
      });

      it('should return initial array if no SensibiisationInternet is added', () => {
        const sensibiisationInternetCollection: ISensibiisationInternet[] = [{ id: 123 }];
        expectedResult = service.addSensibiisationInternetToCollectionIfMissing(sensibiisationInternetCollection, undefined, null);
        expect(expectedResult).toEqual(sensibiisationInternetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
