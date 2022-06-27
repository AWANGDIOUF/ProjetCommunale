import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResultatExamen, getResultatExamenIdentifier } from '../resultat-examen.model';

export type EntityResponseType = HttpResponse<IResultatExamen>;
export type EntityArrayResponseType = HttpResponse<IResultatExamen[]>;

@Injectable({ providedIn: 'root' })
export class ResultatExamenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resultat-examen');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resultatExamen: IResultatExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resultatExamen);
    return this.http
      .post<IResultatExamen>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(resultatExamen: IResultatExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resultatExamen);
    return this.http
      .put<IResultatExamen>(`${this.resourceUrl}/${getResultatExamenIdentifier(resultatExamen) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(resultatExamen: IResultatExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resultatExamen);
    return this.http
      .patch<IResultatExamen>(`${this.resourceUrl}/${getResultatExamenIdentifier(resultatExamen) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IResultatExamen>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResultatExamen[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResultatExamenToCollectionIfMissing(
    resultatExamenCollection: IResultatExamen[],
    ...resultatExamenToCheck: (IResultatExamen | null | undefined)[]
  ): IResultatExamen[] {
    const resultatExamen: IResultatExamen[] = resultatExamenToCheck.filter(isPresent);
    if (resultatExamen.length > 0) {
      const resultatExamenCollectionIdentifiers = resultatExamenCollection.map(
        resultatExamenItem => getResultatExamenIdentifier(resultatExamenItem)!
      );
      const resultatExamenToAdd = resultatExamen.filter(resultatExamenItem => {
        const resultatExamenIdentifier = getResultatExamenIdentifier(resultatExamenItem);
        if (resultatExamenIdentifier == null || resultatExamenCollectionIdentifiers.includes(resultatExamenIdentifier)) {
          return false;
        }
        resultatExamenCollectionIdentifiers.push(resultatExamenIdentifier);
        return true;
      });
      return [...resultatExamenToAdd, ...resultatExamenCollection];
    }
    return resultatExamenCollection;
  }

  protected convertDateFromClient(resultatExamen: IResultatExamen): IResultatExamen {
    return Object.assign({}, resultatExamen, {
      annee: resultatExamen.annee?.isValid() ? resultatExamen.annee.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.annee = res.body.annee ? dayjs(res.body.annee) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((resultatExamen: IResultatExamen) => {
        resultatExamen.annee = resultatExamen.annee ? dayjs(resultatExamen.annee) : undefined;
      });
    }
    return res;
  }
}
