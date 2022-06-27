import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvenement, getEvenementIdentifier } from '../evenement.model';

export type EntityResponseType = HttpResponse<IEvenement>;
export type EntityArrayResponseType = HttpResponse<IEvenement[]>;

@Injectable({ providedIn: 'root' })
export class EvenementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evenements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evenement: IEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenement);
    return this.http
      .post<IEvenement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(evenement: IEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenement);
    return this.http
      .put<IEvenement>(`${this.resourceUrl}/${getEvenementIdentifier(evenement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(evenement: IEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenement);
    return this.http
      .patch<IEvenement>(`${this.resourceUrl}/${getEvenementIdentifier(evenement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvenement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEvenementToCollectionIfMissing(
    evenementCollection: IEvenement[],
    ...evenementsToCheck: (IEvenement | null | undefined)[]
  ): IEvenement[] {
    const evenements: IEvenement[] = evenementsToCheck.filter(isPresent);
    if (evenements.length > 0) {
      const evenementCollectionIdentifiers = evenementCollection.map(evenementItem => getEvenementIdentifier(evenementItem)!);
      const evenementsToAdd = evenements.filter(evenementItem => {
        const evenementIdentifier = getEvenementIdentifier(evenementItem);
        if (evenementIdentifier == null || evenementCollectionIdentifiers.includes(evenementIdentifier)) {
          return false;
        }
        evenementCollectionIdentifiers.push(evenementIdentifier);
        return true;
      });
      return [...evenementsToAdd, ...evenementCollection];
    }
    return evenementCollection;
  }

  protected convertDateFromClient(evenement: IEvenement): IEvenement {
    return Object.assign({}, evenement, {
      delaiInstruction: evenement.delaiInstruction?.isValid() ? evenement.delaiInstruction.toJSON() : undefined,
      delaiInscription: evenement.delaiInscription?.isValid() ? evenement.delaiInscription.toJSON() : undefined,
      delaiValidation: evenement.delaiValidation?.isValid() ? evenement.delaiValidation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.delaiInstruction = res.body.delaiInstruction ? dayjs(res.body.delaiInstruction) : undefined;
      res.body.delaiInscription = res.body.delaiInscription ? dayjs(res.body.delaiInscription) : undefined;
      res.body.delaiValidation = res.body.delaiValidation ? dayjs(res.body.delaiValidation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((evenement: IEvenement) => {
        evenement.delaiInstruction = evenement.delaiInstruction ? dayjs(evenement.delaiInstruction) : undefined;
        evenement.delaiInscription = evenement.delaiInscription ? dayjs(evenement.delaiInscription) : undefined;
        evenement.delaiValidation = evenement.delaiValidation ? dayjs(evenement.delaiValidation) : undefined;
      });
    }
    return res;
  }
}
