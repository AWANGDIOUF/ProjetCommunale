import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICalendrierEvenement, getCalendrierEvenementIdentifier } from '../calendrier-evenement.model';

export type EntityResponseType = HttpResponse<ICalendrierEvenement>;
export type EntityArrayResponseType = HttpResponse<ICalendrierEvenement[]>;

@Injectable({ providedIn: 'root' })
export class CalendrierEvenementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/calendrier-evenements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(calendrierEvenement: ICalendrierEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendrierEvenement);
    return this.http
      .post<ICalendrierEvenement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calendrierEvenement: ICalendrierEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendrierEvenement);
    return this.http
      .put<ICalendrierEvenement>(`${this.resourceUrl}/${getCalendrierEvenementIdentifier(calendrierEvenement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(calendrierEvenement: ICalendrierEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendrierEvenement);
    return this.http
      .patch<ICalendrierEvenement>(`${this.resourceUrl}/${getCalendrierEvenementIdentifier(calendrierEvenement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalendrierEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendrierEvenement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCalendrierEvenementToCollectionIfMissing(
    calendrierEvenementCollection: ICalendrierEvenement[],
    ...calendrierEvenementsToCheck: (ICalendrierEvenement | null | undefined)[]
  ): ICalendrierEvenement[] {
    const calendrierEvenements: ICalendrierEvenement[] = calendrierEvenementsToCheck.filter(isPresent);
    if (calendrierEvenements.length > 0) {
      const calendrierEvenementCollectionIdentifiers = calendrierEvenementCollection.map(
        calendrierEvenementItem => getCalendrierEvenementIdentifier(calendrierEvenementItem)!
      );
      const calendrierEvenementsToAdd = calendrierEvenements.filter(calendrierEvenementItem => {
        const calendrierEvenementIdentifier = getCalendrierEvenementIdentifier(calendrierEvenementItem);
        if (calendrierEvenementIdentifier == null || calendrierEvenementCollectionIdentifiers.includes(calendrierEvenementIdentifier)) {
          return false;
        }
        calendrierEvenementCollectionIdentifiers.push(calendrierEvenementIdentifier);
        return true;
      });
      return [...calendrierEvenementsToAdd, ...calendrierEvenementCollection];
    }
    return calendrierEvenementCollection;
  }

  protected convertDateFromClient(calendrierEvenement: ICalendrierEvenement): ICalendrierEvenement {
    return Object.assign({}, calendrierEvenement, {
      date: calendrierEvenement.date?.isValid() ? calendrierEvenement.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((calendrierEvenement: ICalendrierEvenement) => {
        calendrierEvenement.date = calendrierEvenement.date ? dayjs(calendrierEvenement.date) : undefined;
      });
    }
    return res;
  }
}
