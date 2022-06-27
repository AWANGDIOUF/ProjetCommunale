import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IActivitePolitique, ActivitePolitique } from '../activite-politique.model';

import { ActivitePolitiqueService } from './activite-politique.service';

describe('ActivitePolitique Service', () => {
  let service: ActivitePolitiqueService;
  let httpMock: HttpTestingController;
  let elemDefault: IActivitePolitique;
  let expectedResult: IActivitePolitique | IActivitePolitique[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActivitePolitiqueService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      titreActivite: 'AAAAAAA',
      descriptionActivite: 'AAAAAAA',
      dateDebut: currentDate,
      dateFin: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ActivitePolitique', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.create(new ActivitePolitique()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActivitePolitique', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titreActivite: 'BBBBBB',
          descriptionActivite: 'BBBBBB',
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ActivitePolitique', () => {
      const patchObject = Object.assign(
        {
          dateDebut: currentDate.format(DATE_FORMAT),
        },
        new ActivitePolitique()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActivitePolitique', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titreActivite: 'BBBBBB',
          descriptionActivite: 'BBBBBB',
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ActivitePolitique', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActivitePolitiqueToCollectionIfMissing', () => {
      it('should add a ActivitePolitique to an empty array', () => {
        const activitePolitique: IActivitePolitique = { id: 123 };
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing([], activitePolitique);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activitePolitique);
      });

      it('should not add a ActivitePolitique to an array that contains it', () => {
        const activitePolitique: IActivitePolitique = { id: 123 };
        const activitePolitiqueCollection: IActivitePolitique[] = [
          {
            ...activitePolitique,
          },
          { id: 456 },
        ];
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing(activitePolitiqueCollection, activitePolitique);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActivitePolitique to an array that doesn't contain it", () => {
        const activitePolitique: IActivitePolitique = { id: 123 };
        const activitePolitiqueCollection: IActivitePolitique[] = [{ id: 456 }];
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing(activitePolitiqueCollection, activitePolitique);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activitePolitique);
      });

      it('should add only unique ActivitePolitique to an array', () => {
        const activitePolitiqueArray: IActivitePolitique[] = [{ id: 123 }, { id: 456 }, { id: 18556 }];
        const activitePolitiqueCollection: IActivitePolitique[] = [{ id: 123 }];
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing(activitePolitiqueCollection, ...activitePolitiqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activitePolitique: IActivitePolitique = { id: 123 };
        const activitePolitique2: IActivitePolitique = { id: 456 };
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing([], activitePolitique, activitePolitique2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activitePolitique);
        expect(expectedResult).toContain(activitePolitique2);
      });

      it('should accept null and undefined values', () => {
        const activitePolitique: IActivitePolitique = { id: 123 };
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing([], null, activitePolitique, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activitePolitique);
      });

      it('should return initial array if no ActivitePolitique is added', () => {
        const activitePolitiqueCollection: IActivitePolitique[] = [{ id: 123 }];
        expectedResult = service.addActivitePolitiqueToCollectionIfMissing(activitePolitiqueCollection, undefined, null);
        expect(expectedResult).toEqual(activitePolitiqueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
