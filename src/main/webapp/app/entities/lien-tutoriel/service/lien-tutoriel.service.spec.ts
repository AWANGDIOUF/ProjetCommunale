import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILienTutoriel, LienTutoriel } from '../lien-tutoriel.model';

import { LienTutorielService } from './lien-tutoriel.service';

describe('LienTutoriel Service', () => {
  let service: LienTutorielService;
  let httpMock: HttpTestingController;
  let elemDefault: ILienTutoriel;
  let expectedResult: ILienTutoriel | ILienTutoriel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LienTutorielService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      descriptionLien: 'AAAAAAA',
      lien: 'AAAAAAA',
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

    it('should create a LienTutoriel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LienTutoriel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LienTutoriel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descriptionLien: 'BBBBBB',
          lien: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LienTutoriel', () => {
      const patchObject = Object.assign(
        {
          lien: 'BBBBBB',
        },
        new LienTutoriel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LienTutoriel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descriptionLien: 'BBBBBB',
          lien: 'BBBBBB',
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

    it('should delete a LienTutoriel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLienTutorielToCollectionIfMissing', () => {
      it('should add a LienTutoriel to an empty array', () => {
        const lienTutoriel: ILienTutoriel = { id: 123 };
        expectedResult = service.addLienTutorielToCollectionIfMissing([], lienTutoriel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lienTutoriel);
      });

      it('should not add a LienTutoriel to an array that contains it', () => {
        const lienTutoriel: ILienTutoriel = { id: 123 };
        const lienTutorielCollection: ILienTutoriel[] = [
          {
            ...lienTutoriel,
          },
          { id: 456 },
        ];
        expectedResult = service.addLienTutorielToCollectionIfMissing(lienTutorielCollection, lienTutoriel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LienTutoriel to an array that doesn't contain it", () => {
        const lienTutoriel: ILienTutoriel = { id: 123 };
        const lienTutorielCollection: ILienTutoriel[] = [{ id: 456 }];
        expectedResult = service.addLienTutorielToCollectionIfMissing(lienTutorielCollection, lienTutoriel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lienTutoriel);
      });

      it('should add only unique LienTutoriel to an array', () => {
        const lienTutorielArray: ILienTutoriel[] = [{ id: 123 }, { id: 456 }, { id: 23086 }];
        const lienTutorielCollection: ILienTutoriel[] = [{ id: 123 }];
        expectedResult = service.addLienTutorielToCollectionIfMissing(lienTutorielCollection, ...lienTutorielArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lienTutoriel: ILienTutoriel = { id: 123 };
        const lienTutoriel2: ILienTutoriel = { id: 456 };
        expectedResult = service.addLienTutorielToCollectionIfMissing([], lienTutoriel, lienTutoriel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lienTutoriel);
        expect(expectedResult).toContain(lienTutoriel2);
      });

      it('should accept null and undefined values', () => {
        const lienTutoriel: ILienTutoriel = { id: 123 };
        expectedResult = service.addLienTutorielToCollectionIfMissing([], null, lienTutoriel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lienTutoriel);
      });

      it('should return initial array if no LienTutoriel is added', () => {
        const lienTutorielCollection: ILienTutoriel[] = [{ id: 123 }];
        expectedResult = service.addLienTutorielToCollectionIfMissing(lienTutorielCollection, undefined, null);
        expect(expectedResult).toEqual(lienTutorielCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
