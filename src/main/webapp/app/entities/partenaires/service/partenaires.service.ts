import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartenaires, getPartenairesIdentifier } from '../partenaires.model';

export type EntityResponseType = HttpResponse<IPartenaires>;
export type EntityArrayResponseType = HttpResponse<IPartenaires[]>;

@Injectable({ providedIn: 'root' })
export class PartenairesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/partenaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(partenaires: IPartenaires): Observable<EntityResponseType> {
    return this.http.post<IPartenaires>(this.resourceUrl, partenaires, { observe: 'response' });
  }

  update(partenaires: IPartenaires): Observable<EntityResponseType> {
    return this.http.put<IPartenaires>(`${this.resourceUrl}/${getPartenairesIdentifier(partenaires) as number}`, partenaires, {
      observe: 'response',
    });
  }

  partialUpdate(partenaires: IPartenaires): Observable<EntityResponseType> {
    return this.http.patch<IPartenaires>(`${this.resourceUrl}/${getPartenairesIdentifier(partenaires) as number}`, partenaires, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartenaires>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartenaires[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPartenairesToCollectionIfMissing(
    partenairesCollection: IPartenaires[],
    ...partenairesToCheck: (IPartenaires | null | undefined)[]
  ): IPartenaires[] {
    const partenaires: IPartenaires[] = partenairesToCheck.filter(isPresent);
    if (partenaires.length > 0) {
      const partenairesCollectionIdentifiers = partenairesCollection.map(partenairesItem => getPartenairesIdentifier(partenairesItem)!);
      const partenairesToAdd = partenaires.filter(partenairesItem => {
        const partenairesIdentifier = getPartenairesIdentifier(partenairesItem);
        if (partenairesIdentifier == null || partenairesCollectionIdentifiers.includes(partenairesIdentifier)) {
          return false;
        }
        partenairesCollectionIdentifiers.push(partenairesIdentifier);
        return true;
      });
      return [...partenairesToAdd, ...partenairesCollection];
    }
    return partenairesCollection;
  }
}
