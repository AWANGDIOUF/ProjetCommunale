import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDonneur, getDonneurIdentifier } from '../donneur.model';

export type EntityResponseType = HttpResponse<IDonneur>;
export type EntityArrayResponseType = HttpResponse<IDonneur[]>;

@Injectable({ providedIn: 'root' })
export class DonneurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/donneurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(donneur: IDonneur): Observable<EntityResponseType> {
    return this.http.post<IDonneur>(this.resourceUrl, donneur, { observe: 'response' });
  }

  update(donneur: IDonneur): Observable<EntityResponseType> {
    return this.http.put<IDonneur>(`${this.resourceUrl}/${getDonneurIdentifier(donneur) as number}`, donneur, { observe: 'response' });
  }

  partialUpdate(donneur: IDonneur): Observable<EntityResponseType> {
    return this.http.patch<IDonneur>(`${this.resourceUrl}/${getDonneurIdentifier(donneur) as number}`, donneur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDonneur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDonneur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDonneurToCollectionIfMissing(donneurCollection: IDonneur[], ...donneursToCheck: (IDonneur | null | undefined)[]): IDonneur[] {
    const donneurs: IDonneur[] = donneursToCheck.filter(isPresent);
    if (donneurs.length > 0) {
      const donneurCollectionIdentifiers = donneurCollection.map(donneurItem => getDonneurIdentifier(donneurItem)!);
      const donneursToAdd = donneurs.filter(donneurItem => {
        const donneurIdentifier = getDonneurIdentifier(donneurItem);
        if (donneurIdentifier == null || donneurCollectionIdentifiers.includes(donneurIdentifier)) {
          return false;
        }
        donneurCollectionIdentifiers.push(donneurIdentifier);
        return true;
      });
      return [...donneursToAdd, ...donneurCollection];
    }
    return donneurCollection;
  }
}
