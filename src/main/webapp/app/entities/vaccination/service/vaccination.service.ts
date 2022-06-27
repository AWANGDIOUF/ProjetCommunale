import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVaccination, getVaccinationIdentifier } from '../vaccination.model';

export type EntityResponseType = HttpResponse<IVaccination>;
export type EntityArrayResponseType = HttpResponse<IVaccination[]>;

@Injectable({ providedIn: 'root' })
export class VaccinationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vaccinations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vaccination: IVaccination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccination);
    return this.http
      .post<IVaccination>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vaccination: IVaccination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccination);
    return this.http
      .put<IVaccination>(`${this.resourceUrl}/${getVaccinationIdentifier(vaccination) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vaccination: IVaccination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vaccination);
    return this.http
      .patch<IVaccination>(`${this.resourceUrl}/${getVaccinationIdentifier(vaccination) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVaccination>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVaccination[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVaccinationToCollectionIfMissing(
    vaccinationCollection: IVaccination[],
    ...vaccinationsToCheck: (IVaccination | null | undefined)[]
  ): IVaccination[] {
    const vaccinations: IVaccination[] = vaccinationsToCheck.filter(isPresent);
    if (vaccinations.length > 0) {
      const vaccinationCollectionIdentifiers = vaccinationCollection.map(vaccinationItem => getVaccinationIdentifier(vaccinationItem)!);
      const vaccinationsToAdd = vaccinations.filter(vaccinationItem => {
        const vaccinationIdentifier = getVaccinationIdentifier(vaccinationItem);
        if (vaccinationIdentifier == null || vaccinationCollectionIdentifiers.includes(vaccinationIdentifier)) {
          return false;
        }
        vaccinationCollectionIdentifiers.push(vaccinationIdentifier);
        return true;
      });
      return [...vaccinationsToAdd, ...vaccinationCollection];
    }
    return vaccinationCollection;
  }

  protected convertDateFromClient(vaccination: IVaccination): IVaccination {
    return Object.assign({}, vaccination, {
      date: vaccination.date?.isValid() ? vaccination.date.format(DATE_FORMAT) : undefined,
      dateDebut: vaccination.dateDebut?.isValid() ? vaccination.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: vaccination.dateFin?.isValid() ? vaccination.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vaccination: IVaccination) => {
        vaccination.date = vaccination.date ? dayjs(vaccination.date) : undefined;
        vaccination.dateDebut = vaccination.dateDebut ? dayjs(vaccination.dateDebut) : undefined;
        vaccination.dateFin = vaccination.dateFin ? dayjs(vaccination.dateFin) : undefined;
      });
    }
    return res;
  }
}
