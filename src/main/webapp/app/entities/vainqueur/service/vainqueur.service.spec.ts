import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVainqueur, Vainqueur } from '../vainqueur.model';

import { VainqueurService } from './vainqueur.service';

describe('Vainqueur Service', () => {
  let service: VainqueurService;
  let httpMock: HttpTestingController;
  let elemDefault: IVainqueur;
  let expectedResult: IVainqueur | IVainqueur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VainqueurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      prix: 0,
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

    it('should create a Vainqueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Vainqueur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vainqueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          prix: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vainqueur', () => {
      const patchObject = Object.assign({}, new Vainqueur());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vainqueur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          prix: 1,
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

    it('should delete a Vainqueur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVainqueurToCollectionIfMissing', () => {
      it('should add a Vainqueur to an empty array', () => {
        const vainqueur: IVainqueur = { id: 123 };
        expectedResult = service.addVainqueurToCollectionIfMissing([], vainqueur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vainqueur);
      });

      it('should not add a Vainqueur to an array that contains it', () => {
        const vainqueur: IVainqueur = { id: 123 };
        const vainqueurCollection: IVainqueur[] = [
          {
            ...vainqueur,
          },
          { id: 456 },
        ];
        expectedResult = service.addVainqueurToCollectionIfMissing(vainqueurCollection, vainqueur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vainqueur to an array that doesn't contain it", () => {
        const vainqueur: IVainqueur = { id: 123 };
        const vainqueurCollection: IVainqueur[] = [{ id: 456 }];
        expectedResult = service.addVainqueurToCollectionIfMissing(vainqueurCollection, vainqueur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vainqueur);
      });

      it('should add only unique Vainqueur to an array', () => {
        const vainqueurArray: IVainqueur[] = [{ id: 123 }, { id: 456 }, { id: 41209 }];
        const vainqueurCollection: IVainqueur[] = [{ id: 123 }];
        expectedResult = service.addVainqueurToCollectionIfMissing(vainqueurCollection, ...vainqueurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vainqueur: IVainqueur = { id: 123 };
        const vainqueur2: IVainqueur = { id: 456 };
        expectedResult = service.addVainqueurToCollectionIfMissing([], vainqueur, vainqueur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vainqueur);
        expect(expectedResult).toContain(vainqueur2);
      });

      it('should accept null and undefined values', () => {
        const vainqueur: IVainqueur = { id: 123 };
        expectedResult = service.addVainqueurToCollectionIfMissing([], null, vainqueur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vainqueur);
      });

      it('should return initial array if no Vainqueur is added', () => {
        const vainqueurCollection: IVainqueur[] = [{ id: 123 }];
        expectedResult = service.addVainqueurToCollectionIfMissing(vainqueurCollection, undefined, null);
        expect(expectedResult).toEqual(vainqueurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
