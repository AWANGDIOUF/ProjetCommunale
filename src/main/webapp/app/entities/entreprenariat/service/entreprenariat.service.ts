import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntreprenariat, getEntreprenariatIdentifier } from '../entreprenariat.model';

export type EntityResponseType = HttpResponse<IEntreprenariat>;
export type EntityArrayResponseType = HttpResponse<IEntreprenariat[]>;

@Injectable({ providedIn: 'root' })
export class EntreprenariatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/entreprenariats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(entreprenariat: IEntreprenariat): Observable<EntityResponseType> {
    return this.http.post<IEntreprenariat>(this.resourceUrl, entreprenariat, { observe: 'response' });
  }

  update(entreprenariat: IEntreprenariat): Observable<EntityResponseType> {
    return this.http.put<IEntreprenariat>(`${this.resourceUrl}/${getEntreprenariatIdentifier(entreprenariat) as number}`, entreprenariat, {
      observe: 'response',
    });
  }

  partialUpdate(entreprenariat: IEntreprenariat): Observable<EntityResponseType> {
    return this.http.patch<IEntreprenariat>(
      `${this.resourceUrl}/${getEntreprenariatIdentifier(entreprenariat) as number}`,
      entreprenariat,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntreprenariat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntreprenariat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEntreprenariatToCollectionIfMissing(
    entreprenariatCollection: IEntreprenariat[],
    ...entreprenariatsToCheck: (IEntreprenariat | null | undefined)[]
  ): IEntreprenariat[] {
    const entreprenariats: IEntreprenariat[] = entreprenariatsToCheck.filter(isPresent);
    if (entreprenariats.length > 0) {
      const entreprenariatCollectionIdentifiers = entreprenariatCollection.map(
        entreprenariatItem => getEntreprenariatIdentifier(entreprenariatItem)!
      );
      const entreprenariatsToAdd = entreprenariats.filter(entreprenariatItem => {
        const entreprenariatIdentifier = getEntreprenariatIdentifier(entreprenariatItem);
        if (entreprenariatIdentifier == null || entreprenariatCollectionIdentifiers.includes(entreprenariatIdentifier)) {
          return false;
        }
        entreprenariatCollectionIdentifiers.push(entreprenariatIdentifier);
        return true;
      });
      return [...entreprenariatsToAdd, ...entreprenariatCollection];
    }
    return entreprenariatCollection;
  }
}
