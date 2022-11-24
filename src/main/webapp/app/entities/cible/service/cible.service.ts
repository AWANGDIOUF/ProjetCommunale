import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICible, getCibleIdentifier } from '../cible.model';

export type EntityResponseType = HttpResponse<ICible>;
export type EntityArrayResponseType = HttpResponse<ICible[]>;

@Injectable({ providedIn: 'root' })
export class CibleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cibles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cible: ICible): Observable<EntityResponseType> {
    return this.http.post<ICible>(this.resourceUrl, cible, { observe: 'response' });
  }

  update(cible: ICible): Observable<EntityResponseType> {
    return this.http.put<ICible>(`${this.resourceUrl}/${getCibleIdentifier(cible) as number}`, cible, { observe: 'response' });
  }

  partialUpdate(cible: ICible): Observable<EntityResponseType> {
    return this.http.patch<ICible>(`${this.resourceUrl}/${getCibleIdentifier(cible) as number}`, cible, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICible>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICible[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCibleToCollectionIfMissing(cibleCollection: ICible[], ...ciblesToCheck: (ICible | null | undefined)[]): ICible[] {
    const cibles: ICible[] = ciblesToCheck.filter(isPresent);
    if (cibles.length > 0) {
      const cibleCollectionIdentifiers = cibleCollection.map(cibleItem => getCibleIdentifier(cibleItem)!);
      const ciblesToAdd = cibles.filter(cibleItem => {
        const cibleIdentifier = getCibleIdentifier(cibleItem);
        if (cibleIdentifier == null || cibleCollectionIdentifiers.includes(cibleIdentifier)) {
          return false;
        }
        cibleCollectionIdentifiers.push(cibleIdentifier);
        return true;
      });
      return [...ciblesToAdd, ...cibleCollection];
    }
    return cibleCollection;
  }
}
