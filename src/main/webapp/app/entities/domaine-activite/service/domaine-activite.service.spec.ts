import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDomaineActivite, DomaineActivite } from '../domaine-activite.model';

import { DomaineActiviteService } from './domaine-activite.service';

describe('DomaineActivite Service', () => {
  let service: DomaineActiviteService;
  let httpMock: HttpTestingController;
  let elemDefault: IDomaineActivite;
  let expectedResult: IDomaineActivite | IDomaineActivite[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DomaineActiviteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      typeActivite: 'AAAAAAA',
      description: 'AAAAAAA',
      dateActivite: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateActivite: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DomaineActivite', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateActivite: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateActivite: currentDate,
        },
        returnedFromService
      );

      service.create(new DomaineActivite()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DomaineActivite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeActivite: 'BBBBBB',
          description: 'BBBBBB',
          dateActivite: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateActivite: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DomaineActivite', () => {
      const patchObject = Object.assign({}, new DomaineActivite());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateActivite: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DomaineActivite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeActivite: 'BBBBBB',
          description: 'BBBBBB',
          dateActivite: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateActivite: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DomaineActivite', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDomaineActiviteToCollectionIfMissing', () => {
      it('should add a DomaineActivite to an empty array', () => {
        const domaineActivite: IDomaineActivite = { id: 123 };
        expectedResult = service.addDomaineActiviteToCollectionIfMissing([], domaineActivite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domaineActivite);
      });

      it('should not add a DomaineActivite to an array that contains it', () => {
        const domaineActivite: IDomaineActivite = { id: 123 };
        const domaineActiviteCollection: IDomaineActivite[] = [
          {
            ...domaineActivite,
          },
          { id: 456 },
        ];
        expectedResult = service.addDomaineActiviteToCollectionIfMissing(domaineActiviteCollection, domaineActivite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DomaineActivite to an array that doesn't contain it", () => {
        const domaineActivite: IDomaineActivite = { id: 123 };
        const domaineActiviteCollection: IDomaineActivite[] = [{ id: 456 }];
        expectedResult = service.addDomaineActiviteToCollectionIfMissing(domaineActiviteCollection, domaineActivite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domaineActivite);
      });

      it('should add only unique DomaineActivite to an array', () => {
        const domaineActiviteArray: IDomaineActivite[] = [{ id: 123 }, { id: 456 }, { id: 53504 }];
        const domaineActiviteCollection: IDomaineActivite[] = [{ id: 123 }];
        expectedResult = service.addDomaineActiviteToCollectionIfMissing(domaineActiviteCollection, ...domaineActiviteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const domaineActivite: IDomaineActivite = { id: 123 };
        const domaineActivite2: IDomaineActivite = { id: 456 };
        expectedResult = service.addDomaineActiviteToCollectionIfMissing([], domaineActivite, domaineActivite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domaineActivite);
        expect(expectedResult).toContain(domaineActivite2);
      });

      it('should accept null and undefined values', () => {
        const domaineActivite: IDomaineActivite = { id: 123 };
        expectedResult = service.addDomaineActiviteToCollectionIfMissing([], null, domaineActivite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domaineActivite);
      });

      it('should return initial array if no DomaineActivite is added', () => {
        const domaineActiviteCollection: IDomaineActivite[] = [{ id: 123 }];
        expectedResult = service.addDomaineActiviteToCollectionIfMissing(domaineActiviteCollection, undefined, null);
        expect(expectedResult).toEqual(domaineActiviteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
