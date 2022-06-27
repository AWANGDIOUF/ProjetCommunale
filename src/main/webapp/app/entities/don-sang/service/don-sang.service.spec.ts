import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDonSang, DonSang } from '../don-sang.model';

import { DonSangService } from './don-sang.service';

describe('DonSang Service', () => {
  let service: DonSangService;
  let httpMock: HttpTestingController;
  let elemDefault: IDonSang;
  let expectedResult: IDonSang | IDonSang[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DonSangService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      organisateur: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a DonSang', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DonSang()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DonSang', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          organisateur: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DonSang', () => {
      const patchObject = Object.assign(
        {
          organisateur: 'BBBBBB',
        },
        new DonSang()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DonSang', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          organisateur: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a DonSang', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDonSangToCollectionIfMissing', () => {
      it('should add a DonSang to an empty array', () => {
        const donSang: IDonSang = { id: 123 };
        expectedResult = service.addDonSangToCollectionIfMissing([], donSang);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donSang);
      });

      it('should not add a DonSang to an array that contains it', () => {
        const donSang: IDonSang = { id: 123 };
        const donSangCollection: IDonSang[] = [
          {
            ...donSang,
          },
          { id: 456 },
        ];
        expectedResult = service.addDonSangToCollectionIfMissing(donSangCollection, donSang);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DonSang to an array that doesn't contain it", () => {
        const donSang: IDonSang = { id: 123 };
        const donSangCollection: IDonSang[] = [{ id: 456 }];
        expectedResult = service.addDonSangToCollectionIfMissing(donSangCollection, donSang);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donSang);
      });

      it('should add only unique DonSang to an array', () => {
        const donSangArray: IDonSang[] = [{ id: 123 }, { id: 456 }, { id: 59842 }];
        const donSangCollection: IDonSang[] = [{ id: 123 }];
        expectedResult = service.addDonSangToCollectionIfMissing(donSangCollection, ...donSangArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const donSang: IDonSang = { id: 123 };
        const donSang2: IDonSang = { id: 456 };
        expectedResult = service.addDonSangToCollectionIfMissing([], donSang, donSang2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donSang);
        expect(expectedResult).toContain(donSang2);
      });

      it('should accept null and undefined values', () => {
        const donSang: IDonSang = { id: 123 };
        expectedResult = service.addDonSangToCollectionIfMissing([], null, donSang, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donSang);
      });

      it('should return initial array if no DonSang is added', () => {
        const donSangCollection: IDonSang[] = [{ id: 123 }];
        expectedResult = service.addDonSangToCollectionIfMissing(donSangCollection, undefined, null);
        expect(expectedResult).toEqual(donSangCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
