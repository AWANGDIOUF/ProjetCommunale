import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Domaine } from 'app/entities/enumerations/domaine.model';
import { IArtiste, Artiste } from '../artiste.model';

import { ArtisteService } from './artiste.service';

describe('Artiste Service', () => {
  let service: ArtisteService;
  let httpMock: HttpTestingController;
  let elemDefault: IArtiste;
  let expectedResult: IArtiste | IArtiste[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArtisteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomArtiste: 'AAAAAAA',
      prenomArtiste: 'AAAAAAA',
      domaine: Domaine.Chanteurs,
      autreDomaine: 'AAAAAAA',
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

    it('should create a Artiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Artiste()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Artiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomArtiste: 'BBBBBB',
          prenomArtiste: 'BBBBBB',
          domaine: 'BBBBBB',
          autreDomaine: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Artiste', () => {
      const patchObject = Object.assign(
        {
          nomArtiste: 'BBBBBB',
          domaine: 'BBBBBB',
          autreDomaine: 'BBBBBB',
        },
        new Artiste()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Artiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomArtiste: 'BBBBBB',
          prenomArtiste: 'BBBBBB',
          domaine: 'BBBBBB',
          autreDomaine: 'BBBBBB',
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

    it('should delete a Artiste', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addArtisteToCollectionIfMissing', () => {
      it('should add a Artiste to an empty array', () => {
        const artiste: IArtiste = { id: 123 };
        expectedResult = service.addArtisteToCollectionIfMissing([], artiste);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(artiste);
      });

      it('should not add a Artiste to an array that contains it', () => {
        const artiste: IArtiste = { id: 123 };
        const artisteCollection: IArtiste[] = [
          {
            ...artiste,
          },
          { id: 456 },
        ];
        expectedResult = service.addArtisteToCollectionIfMissing(artisteCollection, artiste);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Artiste to an array that doesn't contain it", () => {
        const artiste: IArtiste = { id: 123 };
        const artisteCollection: IArtiste[] = [{ id: 456 }];
        expectedResult = service.addArtisteToCollectionIfMissing(artisteCollection, artiste);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(artiste);
      });

      it('should add only unique Artiste to an array', () => {
        const artisteArray: IArtiste[] = [{ id: 123 }, { id: 456 }, { id: 58585 }];
        const artisteCollection: IArtiste[] = [{ id: 123 }];
        expectedResult = service.addArtisteToCollectionIfMissing(artisteCollection, ...artisteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const artiste: IArtiste = { id: 123 };
        const artiste2: IArtiste = { id: 456 };
        expectedResult = service.addArtisteToCollectionIfMissing([], artiste, artiste2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(artiste);
        expect(expectedResult).toContain(artiste2);
      });

      it('should accept null and undefined values', () => {
        const artiste: IArtiste = { id: 123 };
        expectedResult = service.addArtisteToCollectionIfMissing([], null, artiste, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(artiste);
      });

      it('should return initial array if no Artiste is added', () => {
        const artisteCollection: IArtiste[] = [{ id: 123 }];
        expectedResult = service.addArtisteToCollectionIfMissing(artisteCollection, undefined, null);
        expect(expectedResult).toEqual(artisteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
