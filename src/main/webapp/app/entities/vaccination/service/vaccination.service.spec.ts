import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVaccination, Vaccination } from '../vaccination.model';

import { VaccinationService } from './vaccination.service';

describe('Service Tests', () => {
  describe('Vaccination Service', () => {
    let service: VaccinationService;
    let httpMock: HttpTestingController;
    let elemDefault: IVaccination;
    let expectedResult: IVaccination | IVaccination[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VaccinationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        description: 'AAAAAAA',
        duree: false,
        dateDebut: currentDate,
        dateFin: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vaccination', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.create(new Vaccination()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vaccination', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            duree: true,
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Vaccination', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            duree: true,
          },
          new Vaccination()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vaccination', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            duree: true,
            dateDebut: currentDate.format(DATE_FORMAT),
            dateFin: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vaccination', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVaccinationToCollectionIfMissing', () => {
        it('should add a Vaccination to an empty array', () => {
          const vaccination: IVaccination = { id: 123 };
          expectedResult = service.addVaccinationToCollectionIfMissing([], vaccination);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vaccination);
        });

        it('should not add a Vaccination to an array that contains it', () => {
          const vaccination: IVaccination = { id: 123 };
          const vaccinationCollection: IVaccination[] = [
            {
              ...vaccination,
            },
            { id: 456 },
          ];
          expectedResult = service.addVaccinationToCollectionIfMissing(vaccinationCollection, vaccination);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vaccination to an array that doesn't contain it", () => {
          const vaccination: IVaccination = { id: 123 };
          const vaccinationCollection: IVaccination[] = [{ id: 456 }];
          expectedResult = service.addVaccinationToCollectionIfMissing(vaccinationCollection, vaccination);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vaccination);
        });

        it('should add only unique Vaccination to an array', () => {
          const vaccinationArray: IVaccination[] = [{ id: 123 }, { id: 456 }, { id: 99309 }];
          const vaccinationCollection: IVaccination[] = [{ id: 123 }];
          expectedResult = service.addVaccinationToCollectionIfMissing(vaccinationCollection, ...vaccinationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vaccination: IVaccination = { id: 123 };
          const vaccination2: IVaccination = { id: 456 };
          expectedResult = service.addVaccinationToCollectionIfMissing([], vaccination, vaccination2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vaccination);
          expect(expectedResult).toContain(vaccination2);
        });

        it('should accept null and undefined values', () => {
          const vaccination: IVaccination = { id: 123 };
          expectedResult = service.addVaccinationToCollectionIfMissing([], null, vaccination, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vaccination);
        });

        it('should return initial array if no Vaccination is added', () => {
          const vaccinationCollection: IVaccination[] = [{ id: 123 }];
          expectedResult = service.addVaccinationToCollectionIfMissing(vaccinationCollection, undefined, null);
          expect(expectedResult).toEqual(vaccinationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
