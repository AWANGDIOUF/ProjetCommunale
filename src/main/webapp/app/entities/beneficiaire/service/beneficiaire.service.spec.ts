import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeBeneficiaire } from 'app/entities/enumerations/type-beneficiaire.model';
import { TypeMoral } from 'app/entities/enumerations/type-moral.model';
import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';

import { BeneficiaireService } from './beneficiaire.service';

describe('Service Tests', () => {
  describe('Beneficiaire Service', () => {
    let service: BeneficiaireService;
    let httpMock: HttpTestingController;
    let elemDefault: IBeneficiaire;
    let expectedResult: IBeneficiaire | IBeneficiaire[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BeneficiaireService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        typeBenefiaire: TypeBeneficiaire.PersonneMoral,
        typePersoMoral: TypeMoral.Groupement,
        prenom: 'AAAAAAA',
        nom: 'AAAAAAA',
        cin: 'AAAAAAA',
        adresse: 'AAAAAAA',
        tel1: 'AAAAAAA',
        autretel1: 'AAAAAAA',
        emailAssociation: 'AAAAAAA',
        nomPresident: 'AAAAAAA',
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

      it('should create a Beneficiaire', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Beneficiaire()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Beneficiaire', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBenefiaire: 'BBBBBB',
            typePersoMoral: 'BBBBBB',
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            cin: 'BBBBBB',
            adresse: 'BBBBBB',
            tel1: 'BBBBBB',
            autretel1: 'BBBBBB',
            emailAssociation: 'BBBBBB',
            nomPresident: 'BBBBBB',
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

      it('should partial update a Beneficiaire', () => {
        const patchObject = Object.assign(
          {
            cin: 'BBBBBB',
            tel1: 'BBBBBB',
            emailAssociation: 'BBBBBB',
            nomPresident: 'BBBBBB',
            description: 'BBBBBB',
          },
          new Beneficiaire()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Beneficiaire', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBenefiaire: 'BBBBBB',
            typePersoMoral: 'BBBBBB',
            prenom: 'BBBBBB',
            nom: 'BBBBBB',
            cin: 'BBBBBB',
            adresse: 'BBBBBB',
            tel1: 'BBBBBB',
            autretel1: 'BBBBBB',
            emailAssociation: 'BBBBBB',
            nomPresident: 'BBBBBB',
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

      it('should delete a Beneficiaire', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBeneficiaireToCollectionIfMissing', () => {
        it('should add a Beneficiaire to an empty array', () => {
          const beneficiaire: IBeneficiaire = { id: 123 };
          expectedResult = service.addBeneficiaireToCollectionIfMissing([], beneficiaire);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(beneficiaire);
        });

        it('should not add a Beneficiaire to an array that contains it', () => {
          const beneficiaire: IBeneficiaire = { id: 123 };
          const beneficiaireCollection: IBeneficiaire[] = [
            {
              ...beneficiaire,
            },
            { id: 456 },
          ];
          expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, beneficiaire);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Beneficiaire to an array that doesn't contain it", () => {
          const beneficiaire: IBeneficiaire = { id: 123 };
          const beneficiaireCollection: IBeneficiaire[] = [{ id: 456 }];
          expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, beneficiaire);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(beneficiaire);
        });

        it('should add only unique Beneficiaire to an array', () => {
          const beneficiaireArray: IBeneficiaire[] = [{ id: 123 }, { id: 456 }, { id: 53038 }];
          const beneficiaireCollection: IBeneficiaire[] = [{ id: 123 }];
          expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, ...beneficiaireArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const beneficiaire: IBeneficiaire = { id: 123 };
          const beneficiaire2: IBeneficiaire = { id: 456 };
          expectedResult = service.addBeneficiaireToCollectionIfMissing([], beneficiaire, beneficiaire2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(beneficiaire);
          expect(expectedResult).toContain(beneficiaire2);
        });

        it('should accept null and undefined values', () => {
          const beneficiaire: IBeneficiaire = { id: 123 };
          expectedResult = service.addBeneficiaireToCollectionIfMissing([], null, beneficiaire, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(beneficiaire);
        });

        it('should return initial array if no Beneficiaire is added', () => {
          const beneficiaireCollection: IBeneficiaire[] = [{ id: 123 }];
          expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, undefined, null);
          expect(expectedResult).toEqual(beneficiaireCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
