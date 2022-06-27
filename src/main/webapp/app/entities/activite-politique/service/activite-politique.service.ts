import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivitePolitique, getActivitePolitiqueIdentifier } from '../activite-politique.model';

export type EntityResponseType = HttpResponse<IActivitePolitique>;
export type EntityArrayResponseType = HttpResponse<IActivitePolitique[]>;

@Injectable({ providedIn: 'root' })
export class ActivitePolitiqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activite-politiques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(activitePolitique: IActivitePolitique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activitePolitique);
    return this.http
      .post<IActivitePolitique>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(activitePolitique: IActivitePolitique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activitePolitique);
    return this.http
      .put<IActivitePolitique>(`${this.resourceUrl}/${getActivitePolitiqueIdentifier(activitePolitique) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(activitePolitique: IActivitePolitique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activitePolitique);
    return this.http
      .patch<IActivitePolitique>(`${this.resourceUrl}/${getActivitePolitiqueIdentifier(activitePolitique) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IActivitePolitique>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IActivitePolitique[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActivitePolitiqueToCollectionIfMissing(
    activitePolitiqueCollection: IActivitePolitique[],
    ...activitePolitiquesToCheck: (IActivitePolitique | null | undefined)[]
  ): IActivitePolitique[] {
    const activitePolitiques: IActivitePolitique[] = activitePolitiquesToCheck.filter(isPresent);
    if (activitePolitiques.length > 0) {
      const activitePolitiqueCollectionIdentifiers = activitePolitiqueCollection.map(
        activitePolitiqueItem => getActivitePolitiqueIdentifier(activitePolitiqueItem)!
      );
      const activitePolitiquesToAdd = activitePolitiques.filter(activitePolitiqueItem => {
        const activitePolitiqueIdentifier = getActivitePolitiqueIdentifier(activitePolitiqueItem);
        if (activitePolitiqueIdentifier == null || activitePolitiqueCollectionIdentifiers.includes(activitePolitiqueIdentifier)) {
          return false;
        }
        activitePolitiqueCollectionIdentifiers.push(activitePolitiqueIdentifier);
        return true;
      });
      return [...activitePolitiquesToAdd, ...activitePolitiqueCollection];
    }
    return activitePolitiqueCollection;
  }

  protected convertDateFromClient(activitePolitique: IActivitePolitique): IActivitePolitique {
    return Object.assign({}, activitePolitique, {
      dateDebut: activitePolitique.dateDebut?.isValid() ? activitePolitique.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: activitePolitique.dateFin?.isValid() ? activitePolitique.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((activitePolitique: IActivitePolitique) => {
        activitePolitique.dateDebut = activitePolitique.dateDebut ? dayjs(activitePolitique.dateDebut) : undefined;
        activitePolitique.dateFin = activitePolitique.dateFin ? dayjs(activitePolitique.dateFin) : undefined;
      });
    }
    return res;
  }
}
