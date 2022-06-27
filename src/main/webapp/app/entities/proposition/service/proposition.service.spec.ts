import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProposition, Proposition } from '../proposition.model';

import { PropositionService } from './proposition.service';

describe('Proposition Service', () => {
  let service: PropositionService;
  let httpMock: HttpTestingController;
  let elemDefault: IProposition;
  let expectedResult: IProposition | IProposition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PropositionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a Proposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Proposition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Proposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a Proposition', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new Proposition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Proposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a Proposition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPropositionToCollectionIfMissing', () => {
      it('should add a Proposition to an empty array', () => {
        const proposition: IProposition = { id: 123 };
        expectedResult = service.addPropositionToCollectionIfMissing([], proposition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proposition);
      });

      it('should not add a Proposition to an array that contains it', () => {
        const proposition: IProposition = { id: 123 };
        const propositionCollection: IProposition[] = [
          {
            ...proposition,
          },
          { id: 456 },
        ];
        expectedResult = service.addPropositionToCollectionIfMissing(propositionCollection, proposition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Proposition to an array that doesn't contain it", () => {
        const proposition: IProposition = { id: 123 };
        const propositionCollection: IProposition[] = [{ id: 456 }];
        expectedResult = service.addPropositionToCollectionIfMissing(propositionCollection, proposition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proposition);
      });

      it('should add only unique Proposition to an array', () => {
        const propositionArray: IProposition[] = [{ id: 123 }, { id: 456 }, { id: 50814 }];
        const propositionCollection: IProposition[] = [{ id: 123 }];
        expectedResult = service.addPropositionToCollectionIfMissing(propositionCollection, ...propositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proposition: IProposition = { id: 123 };
        const proposition2: IProposition = { id: 456 };
        expectedResult = service.addPropositionToCollectionIfMissing([], proposition, proposition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proposition);
        expect(expectedResult).toContain(proposition2);
      });

      it('should accept null and undefined values', () => {
        const proposition: IProposition = { id: 123 };
        expectedResult = service.addPropositionToCollectionIfMissing([], null, proposition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proposition);
      });

      it('should return initial array if no Proposition is added', () => {
        const propositionCollection: IProposition[] = [{ id: 123 }];
        expectedResult = service.addPropositionToCollectionIfMissing(propositionCollection, undefined, null);
        expect(expectedResult).toEqual(propositionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
