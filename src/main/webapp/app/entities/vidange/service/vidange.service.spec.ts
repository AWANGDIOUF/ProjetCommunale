import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVidange, Vidange } from '../vidange.model';

import { VidangeService } from './vidange.service';

describe('Vidange Service', () => {
  let service: VidangeService;
  let httpMock: HttpTestingController;
  let elemDefault: IVidange;
  let expectedResult: IVidange | IVidange[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VidangeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomVideur: 'AAAAAAA',
      prenomVideur: 'AAAAAAA',
      tel1: 'AAAAAAA',
      tel2: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Vidange', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Vidange()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vidange', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomVideur: 'BBBBBB',
          prenomVideur: 'BBBBBB',
          tel1: 'BBBBBB',
          tel2: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vidange', () => {
      const patchObject = Object.assign(
        {
          tel1: 'BBBBBB',
        },
        new Vidange()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vidange', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomVideur: 'BBBBBB',
          prenomVideur: 'BBBBBB',
          tel1: 'BBBBBB',
          tel2: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Vidange', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVidangeToCollectionIfMissing', () => {
      it('should add a Vidange to an empty array', () => {
        const vidange: IVidange = { id: 123 };
        expectedResult = service.addVidangeToCollectionIfMissing([], vidange);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vidange);
      });

      it('should not add a Vidange to an array that contains it', () => {
        const vidange: IVidange = { id: 123 };
        const vidangeCollection: IVidange[] = [
          {
            ...vidange,
          },
          { id: 456 },
        ];
        expectedResult = service.addVidangeToCollectionIfMissing(vidangeCollection, vidange);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vidange to an array that doesn't contain it", () => {
        const vidange: IVidange = { id: 123 };
        const vidangeCollection: IVidange[] = [{ id: 456 }];
        expectedResult = service.addVidangeToCollectionIfMissing(vidangeCollection, vidange);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vidange);
      });

      it('should add only unique Vidange to an array', () => {
        const vidangeArray: IVidange[] = [{ id: 123 }, { id: 456 }, { id: 23910 }];
        const vidangeCollection: IVidange[] = [{ id: 123 }];
        expectedResult = service.addVidangeToCollectionIfMissing(vidangeCollection, ...vidangeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vidange: IVidange = { id: 123 };
        const vidange2: IVidange = { id: 456 };
        expectedResult = service.addVidangeToCollectionIfMissing([], vidange, vidange2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vidange);
        expect(expectedResult).toContain(vidange2);
      });

      it('should accept null and undefined values', () => {
        const vidange: IVidange = { id: 123 };
        expectedResult = service.addVidangeToCollectionIfMissing([], null, vidange, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vidange);
      });

      it('should return initial array if no Vidange is added', () => {
        const vidangeCollection: IVidange[] = [{ id: 123 }];
        expectedResult = service.addVidangeToCollectionIfMissing(vidangeCollection, undefined, null);
        expect(expectedResult).toEqual(vidangeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
