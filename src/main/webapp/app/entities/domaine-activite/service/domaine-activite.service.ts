import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomaineActivite, getDomaineActiviteIdentifier } from '../domaine-activite.model';

export type EntityResponseType = HttpResponse<IDomaineActivite>;
export type EntityArrayResponseType = HttpResponse<IDomaineActivite[]>;

@Injectable({ providedIn: 'root' })
export class DomaineActiviteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domaine-activites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(domaineActivite: IDomaineActivite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(domaineActivite);
    return this.http
      .post<IDomaineActivite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(domaineActivite: IDomaineActivite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(domaineActivite);
    return this.http
      .put<IDomaineActivite>(`${this.resourceUrl}/${getDomaineActiviteIdentifier(domaineActivite) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(domaineActivite: IDomaineActivite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(domaineActivite);
    return this.http
      .patch<IDomaineActivite>(`${this.resourceUrl}/${getDomaineActiviteIdentifier(domaineActivite) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDomaineActivite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDomaineActivite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDomaineActiviteToCollectionIfMissing(
    domaineActiviteCollection: IDomaineActivite[],
    ...domaineActivitesToCheck: (IDomaineActivite | null | undefined)[]
  ): IDomaineActivite[] {
    const domaineActivites: IDomaineActivite[] = domaineActivitesToCheck.filter(isPresent);
    if (domaineActivites.length > 0) {
      const domaineActiviteCollectionIdentifiers = domaineActiviteCollection.map(
        domaineActiviteItem => getDomaineActiviteIdentifier(domaineActiviteItem)!
      );
      const domaineActivitesToAdd = domaineActivites.filter(domaineActiviteItem => {
        const domaineActiviteIdentifier = getDomaineActiviteIdentifier(domaineActiviteItem);
        if (domaineActiviteIdentifier == null || domaineActiviteCollectionIdentifiers.includes(domaineActiviteIdentifier)) {
          return false;
        }
        domaineActiviteCollectionIdentifiers.push(domaineActiviteIdentifier);
        return true;
      });
      return [...domaineActivitesToAdd, ...domaineActiviteCollection];
    }
    return domaineActiviteCollection;
  }

  protected convertDateFromClient(domaineActivite: IDomaineActivite): IDomaineActivite {
    return Object.assign({}, domaineActivite, {
      dateActivite: domaineActivite.dateActivite?.isValid() ? domaineActivite.dateActivite.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateActivite = res.body.dateActivite ? dayjs(res.body.dateActivite) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((domaineActivite: IDomaineActivite) => {
        domaineActivite.dateActivite = domaineActivite.dateActivite ? dayjs(domaineActivite.dateActivite) : undefined;
      });
    }
    return res;
  }
}
