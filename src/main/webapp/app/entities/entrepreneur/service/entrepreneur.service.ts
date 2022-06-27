import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntrepreneur, getEntrepreneurIdentifier } from '../entrepreneur.model';

export type EntityResponseType = HttpResponse<IEntrepreneur>;
export type EntityArrayResponseType = HttpResponse<IEntrepreneur[]>;

@Injectable({ providedIn: 'root' })
export class EntrepreneurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/entrepreneurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(entrepreneur: IEntrepreneur): Observable<EntityResponseType> {
    return this.http.post<IEntrepreneur>(this.resourceUrl, entrepreneur, { observe: 'response' });
  }

  update(entrepreneur: IEntrepreneur): Observable<EntityResponseType> {
    return this.http.put<IEntrepreneur>(`${this.resourceUrl}/${getEntrepreneurIdentifier(entrepreneur) as number}`, entrepreneur, {
      observe: 'response',
    });
  }

  partialUpdate(entrepreneur: IEntrepreneur): Observable<EntityResponseType> {
    return this.http.patch<IEntrepreneur>(`${this.resourceUrl}/${getEntrepreneurIdentifier(entrepreneur) as number}`, entrepreneur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntrepreneur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntrepreneur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEntrepreneurToCollectionIfMissing(
    entrepreneurCollection: IEntrepreneur[],
    ...entrepreneursToCheck: (IEntrepreneur | null | undefined)[]
  ): IEntrepreneur[] {
    const entrepreneurs: IEntrepreneur[] = entrepreneursToCheck.filter(isPresent);
    if (entrepreneurs.length > 0) {
      const entrepreneurCollectionIdentifiers = entrepreneurCollection.map(
        entrepreneurItem => getEntrepreneurIdentifier(entrepreneurItem)!
      );
      const entrepreneursToAdd = entrepreneurs.filter(entrepreneurItem => {
        const entrepreneurIdentifier = getEntrepreneurIdentifier(entrepreneurItem);
        if (entrepreneurIdentifier == null || entrepreneurCollectionIdentifiers.includes(entrepreneurIdentifier)) {
          return false;
        }
        entrepreneurCollectionIdentifiers.push(entrepreneurIdentifier);
        return true;
      });
      return [...entrepreneursToAdd, ...entrepreneurCollection];
    }
    return entrepreneurCollection;
  }
}
