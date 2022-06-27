import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRecuperationRecyclable, RecuperationRecyclable } from '../recuperation-recyclable.model';

import { RecuperationRecyclableService } from './recuperation-recyclable.service';

describe('RecuperationRecyclable Service', () => {
  let service: RecuperationRecyclableService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecuperationRecyclable;
  let expectedResult: IRecuperationRecyclable | IRecuperationRecyclable[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecuperationRecyclableService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
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

    it('should create a RecuperationRecyclable', () => {
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

      service.create(new RecuperationRecyclable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecuperationRecyclable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
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

    it('should partial update a RecuperationRecyclable', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
          lieu: 'BBBBBB',
        },
        new RecuperationRecyclable()
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

    it('should return a list of RecuperationRecyclable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
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

    it('should delete a RecuperationRecyclable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecuperationRecyclableToCollectionIfMissing', () => {
      it('should add a RecuperationRecyclable to an empty array', () => {
        const recuperationRecyclable: IRecuperationRecyclable = { id: 123 };
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing([], recuperationRecyclable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recuperationRecyclable);
      });

      it('should not add a RecuperationRecyclable to an array that contains it', () => {
        const recuperationRecyclable: IRecuperationRecyclable = { id: 123 };
        const recuperationRecyclableCollection: IRecuperationRecyclable[] = [
          {
            ...recuperationRecyclable,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing(recuperationRecyclableCollection, recuperationRecyclable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecuperationRecyclable to an array that doesn't contain it", () => {
        const recuperationRecyclable: IRecuperationRecyclable = { id: 123 };
        const recuperationRecyclableCollection: IRecuperationRecyclable[] = [{ id: 456 }];
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing(recuperationRecyclableCollection, recuperationRecyclable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recuperationRecyclable);
      });

      it('should add only unique RecuperationRecyclable to an array', () => {
        const recuperationRecyclableArray: IRecuperationRecyclable[] = [{ id: 123 }, { id: 456 }, { id: 37547 }];
        const recuperationRecyclableCollection: IRecuperationRecyclable[] = [{ id: 123 }];
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing(
          recuperationRecyclableCollection,
          ...recuperationRecyclableArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recuperationRecyclable: IRecuperationRecyclable = { id: 123 };
        const recuperationRecyclable2: IRecuperationRecyclable = { id: 456 };
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing([], recuperationRecyclable, recuperationRecyclable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recuperationRecyclable);
        expect(expectedResult).toContain(recuperationRecyclable2);
      });

      it('should accept null and undefined values', () => {
        const recuperationRecyclable: IRecuperationRecyclable = { id: 123 };
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing([], null, recuperationRecyclable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recuperationRecyclable);
      });

      it('should return initial array if no RecuperationRecyclable is added', () => {
        const recuperationRecyclableCollection: IRecuperationRecyclable[] = [{ id: 123 }];
        expectedResult = service.addRecuperationRecyclableToCollectionIfMissing(recuperationRecyclableCollection, undefined, null);
        expect(expectedResult).toEqual(recuperationRecyclableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
