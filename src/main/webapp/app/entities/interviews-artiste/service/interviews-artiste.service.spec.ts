import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInterviewsArtiste, InterviewsArtiste } from '../interviews-artiste.model';

import { InterviewsArtisteService } from './interviews-artiste.service';

describe('InterviewsArtiste Service', () => {
  let service: InterviewsArtisteService;
  let httpMock: HttpTestingController;
  let elemDefault: IInterviewsArtiste;
  let expectedResult: IInterviewsArtiste | IInterviewsArtiste[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterviewsArtisteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      titre: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a InterviewsArtiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InterviewsArtiste()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InterviewsArtiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titre: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should partial update a InterviewsArtiste', () => {
      const patchObject = Object.assign(
        {
          titre: 'BBBBBB',
          lien: 'BBBBBB',
        },
        new InterviewsArtiste()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InterviewsArtiste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titre: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a InterviewsArtiste', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInterviewsArtisteToCollectionIfMissing', () => {
      it('should add a InterviewsArtiste to an empty array', () => {
        const interviewsArtiste: IInterviewsArtiste = { id: 123 };
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing([], interviewsArtiste);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interviewsArtiste);
      });

      it('should not add a InterviewsArtiste to an array that contains it', () => {
        const interviewsArtiste: IInterviewsArtiste = { id: 123 };
        const interviewsArtisteCollection: IInterviewsArtiste[] = [
          {
            ...interviewsArtiste,
          },
          { id: 456 },
        ];
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing(interviewsArtisteCollection, interviewsArtiste);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InterviewsArtiste to an array that doesn't contain it", () => {
        const interviewsArtiste: IInterviewsArtiste = { id: 123 };
        const interviewsArtisteCollection: IInterviewsArtiste[] = [{ id: 456 }];
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing(interviewsArtisteCollection, interviewsArtiste);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interviewsArtiste);
      });

      it('should add only unique InterviewsArtiste to an array', () => {
        const interviewsArtisteArray: IInterviewsArtiste[] = [{ id: 123 }, { id: 456 }, { id: 83467 }];
        const interviewsArtisteCollection: IInterviewsArtiste[] = [{ id: 123 }];
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing(interviewsArtisteCollection, ...interviewsArtisteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const interviewsArtiste: IInterviewsArtiste = { id: 123 };
        const interviewsArtiste2: IInterviewsArtiste = { id: 456 };
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing([], interviewsArtiste, interviewsArtiste2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interviewsArtiste);
        expect(expectedResult).toContain(interviewsArtiste2);
      });

      it('should accept null and undefined values', () => {
        const interviewsArtiste: IInterviewsArtiste = { id: 123 };
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing([], null, interviewsArtiste, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interviewsArtiste);
      });

      it('should return initial array if no InterviewsArtiste is added', () => {
        const interviewsArtisteCollection: IInterviewsArtiste[] = [{ id: 123 }];
        expectedResult = service.addInterviewsArtisteToCollectionIfMissing(interviewsArtisteCollection, undefined, null);
        expect(expectedResult).toEqual(interviewsArtisteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
