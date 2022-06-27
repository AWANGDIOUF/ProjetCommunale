import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnsegnant, getEnsegnantIdentifier } from '../ensegnant.model';

export type EntityResponseType = HttpResponse<IEnsegnant>;
export type EntityArrayResponseType = HttpResponse<IEnsegnant[]>;

@Injectable({ providedIn: 'root' })
export class EnsegnantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ensegnants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ensegnant: IEnsegnant): Observable<EntityResponseType> {
    return this.http.post<IEnsegnant>(this.resourceUrl, ensegnant, { observe: 'response' });
  }

  update(ensegnant: IEnsegnant): Observable<EntityResponseType> {
    return this.http.put<IEnsegnant>(`${this.resourceUrl}/${getEnsegnantIdentifier(ensegnant) as number}`, ensegnant, {
      observe: 'response',
    });
  }

  partialUpdate(ensegnant: IEnsegnant): Observable<EntityResponseType> {
    return this.http.patch<IEnsegnant>(`${this.resourceUrl}/${getEnsegnantIdentifier(ensegnant) as number}`, ensegnant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnsegnant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnsegnant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEnsegnantToCollectionIfMissing(
    ensegnantCollection: IEnsegnant[],
    ...ensegnantsToCheck: (IEnsegnant | null | undefined)[]
  ): IEnsegnant[] {
    const ensegnants: IEnsegnant[] = ensegnantsToCheck.filter(isPresent);
    if (ensegnants.length > 0) {
      const ensegnantCollectionIdentifiers = ensegnantCollection.map(ensegnantItem => getEnsegnantIdentifier(ensegnantItem)!);
      const ensegnantsToAdd = ensegnants.filter(ensegnantItem => {
        const ensegnantIdentifier = getEnsegnantIdentifier(ensegnantItem);
        if (ensegnantIdentifier == null || ensegnantCollectionIdentifiers.includes(ensegnantIdentifier)) {
          return false;
        }
        ensegnantCollectionIdentifiers.push(ensegnantIdentifier);
        return true;
      });
      return [...ensegnantsToAdd, ...ensegnantCollection];
    }
    return ensegnantCollection;
  }
}
