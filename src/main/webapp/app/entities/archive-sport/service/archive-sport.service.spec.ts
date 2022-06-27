import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IArchiveSport, ArchiveSport } from '../archive-sport.model';

import { ArchiveSportService } from './archive-sport.service';

describe('ArchiveSport Service', () => {
  let service: ArchiveSportService;
  let httpMock: HttpTestingController;
  let elemDefault: IArchiveSport;
  let expectedResult: IArchiveSport | IArchiveSport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArchiveSportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
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

    it('should create a ArchiveSport', () => {
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

      service.create(new ArchiveSport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArchiveSport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a ArchiveSport', () => {
      const patchObject = Object.assign({}, new ArchiveSport());

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

    it('should return a list of ArchiveSport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a ArchiveSport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addArchiveSportToCollectionIfMissing', () => {
      it('should add a ArchiveSport to an empty array', () => {
        const archiveSport: IArchiveSport = { id: 123 };
        expectedResult = service.addArchiveSportToCollectionIfMissing([], archiveSport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(archiveSport);
      });

      it('should not add a ArchiveSport to an array that contains it', () => {
        const archiveSport: IArchiveSport = { id: 123 };
        const archiveSportCollection: IArchiveSport[] = [
          {
            ...archiveSport,
          },
          { id: 456 },
        ];
        expectedResult = service.addArchiveSportToCollectionIfMissing(archiveSportCollection, archiveSport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArchiveSport to an array that doesn't contain it", () => {
        const archiveSport: IArchiveSport = { id: 123 };
        const archiveSportCollection: IArchiveSport[] = [{ id: 456 }];
        expectedResult = service.addArchiveSportToCollectionIfMissing(archiveSportCollection, archiveSport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(archiveSport);
      });

      it('should add only unique ArchiveSport to an array', () => {
        const archiveSportArray: IArchiveSport[] = [{ id: 123 }, { id: 456 }, { id: 94328 }];
        const archiveSportCollection: IArchiveSport[] = [{ id: 123 }];
        expectedResult = service.addArchiveSportToCollectionIfMissing(archiveSportCollection, ...archiveSportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const archiveSport: IArchiveSport = { id: 123 };
        const archiveSport2: IArchiveSport = { id: 456 };
        expectedResult = service.addArchiveSportToCollectionIfMissing([], archiveSport, archiveSport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(archiveSport);
        expect(expectedResult).toContain(archiveSport2);
      });

      it('should accept null and undefined values', () => {
        const archiveSport: IArchiveSport = { id: 123 };
        expectedResult = service.addArchiveSportToCollectionIfMissing([], null, archiveSport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(archiveSport);
      });

      it('should return initial array if no ArchiveSport is added', () => {
        const archiveSportCollection: IArchiveSport[] = [{ id: 123 }];
        expectedResult = service.addArchiveSportToCollectionIfMissing(archiveSportCollection, undefined, null);
        expect(expectedResult).toEqual(archiveSportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
