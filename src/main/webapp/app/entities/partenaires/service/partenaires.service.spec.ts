import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPartenaires, Partenaires } from '../partenaires.model';

import { PartenairesService } from './partenaires.service';

describe('Partenaires Service', () => {
  let service: PartenairesService;
  let httpMock: HttpTestingController;
  let elemDefault: IPartenaires;
  let expectedResult: IPartenaires | IPartenaires[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartenairesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomPartenaire: 'AAAAAAA',
      emailPartenaire: 'AAAAAAA',
      telPartenaire: 'AAAAAAA',
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

    it('should create a Partenaires', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Partenaires()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Partenaires', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPartenaire: 'BBBBBB',
          emailPartenaire: 'BBBBBB',
          telPartenaire: 'BBBBBB',
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

    it('should partial update a Partenaires', () => {
      const patchObject = Object.assign(
        {
          nomPartenaire: 'BBBBBB',
          emailPartenaire: 'BBBBBB',
          telPartenaire: 'BBBBBB',
        },
        new Partenaires()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Partenaires', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPartenaire: 'BBBBBB',
          emailPartenaire: 'BBBBBB',
          telPartenaire: 'BBBBBB',
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

    it('should delete a Partenaires', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPartenairesToCollectionIfMissing', () => {
      it('should add a Partenaires to an empty array', () => {
        const partenaires: IPartenaires = { id: 123 };
        expectedResult = service.addPartenairesToCollectionIfMissing([], partenaires);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partenaires);
      });

      it('should not add a Partenaires to an array that contains it', () => {
        const partenaires: IPartenaires = { id: 123 };
        const partenairesCollection: IPartenaires[] = [
          {
            ...partenaires,
          },
          { id: 456 },
        ];
        expectedResult = service.addPartenairesToCollectionIfMissing(partenairesCollection, partenaires);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Partenaires to an array that doesn't contain it", () => {
        const partenaires: IPartenaires = { id: 123 };
        const partenairesCollection: IPartenaires[] = [{ id: 456 }];
        expectedResult = service.addPartenairesToCollectionIfMissing(partenairesCollection, partenaires);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partenaires);
      });

      it('should add only unique Partenaires to an array', () => {
        const partenairesArray: IPartenaires[] = [{ id: 123 }, { id: 456 }, { id: 14848 }];
        const partenairesCollection: IPartenaires[] = [{ id: 123 }];
        expectedResult = service.addPartenairesToCollectionIfMissing(partenairesCollection, ...partenairesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partenaires: IPartenaires = { id: 123 };
        const partenaires2: IPartenaires = { id: 456 };
        expectedResult = service.addPartenairesToCollectionIfMissing([], partenaires, partenaires2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partenaires);
        expect(expectedResult).toContain(partenaires2);
      });

      it('should accept null and undefined values', () => {
        const partenaires: IPartenaires = { id: 123 };
        expectedResult = service.addPartenairesToCollectionIfMissing([], null, partenaires, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partenaires);
      });

      it('should return initial array if no Partenaires is added', () => {
        const partenairesCollection: IPartenaires[] = [{ id: 123 }];
        expectedResult = service.addPartenairesToCollectionIfMissing(partenairesCollection, undefined, null);
        expect(expectedResult).toEqual(partenairesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
