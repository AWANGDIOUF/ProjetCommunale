import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICollecteurOdeur, CollecteurOdeur } from '../collecteur-odeur.model';

import { CollecteurOdeurService } from './collecteur-odeur.service';

describe('CollecteurOdeur Service', () => {
  let service: CollecteurOdeurService;
  let httpMock: HttpTestingController;
  let elemDefault: ICollecteurOdeur;
  let expectedResult: ICollecteurOdeur | ICollecteurOdeur[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CollecteurOdeurService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomCollecteur: 'AAAAAAA',
      prenomCollecteur: 'AAAAAAA',
      date: currentDate,
      tel1: 'AAAAAAA',
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

    it('should create a CollecteurOdeur', () => {
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

      service.create(new CollecteurOdeur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CollecteurOdeur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCollecteur: 'BBBBBB',
          prenomCollecteur: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          tel1: 'BBBBBB',
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

    it('should partial update a CollecteurOdeur', () => {
      const patchObject = Object.assign(
        {
          tel1: 'BBBBBB',
        },
        new CollecteurOdeur()
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

    it('should return a list of CollecteurOdeur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCollecteur: 'BBBBBB',
          prenomCollecteur: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          tel1: 'BBBBBB',
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

    it('should delete a CollecteurOdeur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCollecteurOdeurToCollectionIfMissing', () => {
      it('should add a CollecteurOdeur to an empty array', () => {
        const collecteurOdeur: ICollecteurOdeur = { id: 123 };
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing([], collecteurOdeur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collecteurOdeur);
      });

      it('should not add a CollecteurOdeur to an array that contains it', () => {
        const collecteurOdeur: ICollecteurOdeur = { id: 123 };
        const collecteurOdeurCollection: ICollecteurOdeur[] = [
          {
            ...collecteurOdeur,
          },
          { id: 456 },
        ];
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing(collecteurOdeurCollection, collecteurOdeur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CollecteurOdeur to an array that doesn't contain it", () => {
        const collecteurOdeur: ICollecteurOdeur = { id: 123 };
        const collecteurOdeurCollection: ICollecteurOdeur[] = [{ id: 456 }];
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing(collecteurOdeurCollection, collecteurOdeur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collecteurOdeur);
      });

      it('should add only unique CollecteurOdeur to an array', () => {
        const collecteurOdeurArray: ICollecteurOdeur[] = [{ id: 123 }, { id: 456 }, { id: 95253 }];
        const collecteurOdeurCollection: ICollecteurOdeur[] = [{ id: 123 }];
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing(collecteurOdeurCollection, ...collecteurOdeurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const collecteurOdeur: ICollecteurOdeur = { id: 123 };
        const collecteurOdeur2: ICollecteurOdeur = { id: 456 };
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing([], collecteurOdeur, collecteurOdeur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collecteurOdeur);
        expect(expectedResult).toContain(collecteurOdeur2);
      });

      it('should accept null and undefined values', () => {
        const collecteurOdeur: ICollecteurOdeur = { id: 123 };
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing([], null, collecteurOdeur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collecteurOdeur);
      });

      it('should return initial array if no CollecteurOdeur is added', () => {
        const collecteurOdeurCollection: ICollecteurOdeur[] = [{ id: 123 }];
        expectedResult = service.addCollecteurOdeurToCollectionIfMissing(collecteurOdeurCollection, undefined, null);
        expect(expectedResult).toEqual(collecteurOdeurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
