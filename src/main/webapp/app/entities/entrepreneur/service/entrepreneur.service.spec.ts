import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEntrepreneur, Entrepreneur } from '../entrepreneur.model';

import { EntrepreneurService } from './entrepreneur.service';

describe('Entrepreneur Service', () => {
  let service: EntrepreneurService;
  let httpMock: HttpTestingController;
  let elemDefault: IEntrepreneur;
  let expectedResult: IEntrepreneur | IEntrepreneur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EntrepreneurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomEntrepreneur: 'AAAAAAA',
      prenomEntrepreneur: 'AAAAAAA',
      emailEntrepreneur: 'AAAAAAA',
      telEntrepreneur: 'AAAAAAA',
      tel1Entrepreneur: 'AAAAAAA',
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

    it('should create a Entrepreneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Entrepreneur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Entrepreneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEntrepreneur: 'BBBBBB',
          prenomEntrepreneur: 'BBBBBB',
          emailEntrepreneur: 'BBBBBB',
          telEntrepreneur: 'BBBBBB',
          tel1Entrepreneur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Entrepreneur', () => {
      const patchObject = Object.assign(
        {
          emailEntrepreneur: 'BBBBBB',
        },
        new Entrepreneur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Entrepreneur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEntrepreneur: 'BBBBBB',
          prenomEntrepreneur: 'BBBBBB',
          emailEntrepreneur: 'BBBBBB',
          telEntrepreneur: 'BBBBBB',
          tel1Entrepreneur: 'BBBBBB',
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

    it('should delete a Entrepreneur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEntrepreneurToCollectionIfMissing', () => {
      it('should add a Entrepreneur to an empty array', () => {
        const entrepreneur: IEntrepreneur = { id: 123 };
        expectedResult = service.addEntrepreneurToCollectionIfMissing([], entrepreneur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entrepreneur);
      });

      it('should not add a Entrepreneur to an array that contains it', () => {
        const entrepreneur: IEntrepreneur = { id: 123 };
        const entrepreneurCollection: IEntrepreneur[] = [
          {
            ...entrepreneur,
          },
          { id: 456 },
        ];
        expectedResult = service.addEntrepreneurToCollectionIfMissing(entrepreneurCollection, entrepreneur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Entrepreneur to an array that doesn't contain it", () => {
        const entrepreneur: IEntrepreneur = { id: 123 };
        const entrepreneurCollection: IEntrepreneur[] = [{ id: 456 }];
        expectedResult = service.addEntrepreneurToCollectionIfMissing(entrepreneurCollection, entrepreneur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entrepreneur);
      });

      it('should add only unique Entrepreneur to an array', () => {
        const entrepreneurArray: IEntrepreneur[] = [{ id: 123 }, { id: 456 }, { id: 2242 }];
        const entrepreneurCollection: IEntrepreneur[] = [{ id: 123 }];
        expectedResult = service.addEntrepreneurToCollectionIfMissing(entrepreneurCollection, ...entrepreneurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const entrepreneur: IEntrepreneur = { id: 123 };
        const entrepreneur2: IEntrepreneur = { id: 456 };
        expectedResult = service.addEntrepreneurToCollectionIfMissing([], entrepreneur, entrepreneur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(entrepreneur);
        expect(expectedResult).toContain(entrepreneur2);
      });

      it('should accept null and undefined values', () => {
        const entrepreneur: IEntrepreneur = { id: 123 };
        expectedResult = service.addEntrepreneurToCollectionIfMissing([], null, entrepreneur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(entrepreneur);
      });

      it('should return initial array if no Entrepreneur is added', () => {
        const entrepreneurCollection: IEntrepreneur[] = [{ id: 123 }];
        expectedResult = service.addEntrepreneurToCollectionIfMissing(entrepreneurCollection, undefined, null);
        expect(expectedResult).toEqual(entrepreneurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
