import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeSport, getTypeSportIdentifier } from '../type-sport.model';

export type EntityResponseType = HttpResponse<ITypeSport>;
export type EntityArrayResponseType = HttpResponse<ITypeSport[]>;

@Injectable({ providedIn: 'root' })
export class TypeSportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-sports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeSport: ITypeSport): Observable<EntityResponseType> {
    return this.http.post<ITypeSport>(this.resourceUrl, typeSport, { observe: 'response' });
  }

  update(typeSport: ITypeSport): Observable<EntityResponseType> {
    return this.http.put<ITypeSport>(`${this.resourceUrl}/${getTypeSportIdentifier(typeSport) as number}`, typeSport, {
      observe: 'response',
    });
  }

  partialUpdate(typeSport: ITypeSport): Observable<EntityResponseType> {
    return this.http.patch<ITypeSport>(`${this.resourceUrl}/${getTypeSportIdentifier(typeSport) as number}`, typeSport, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeSport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeSport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeSportToCollectionIfMissing(
    typeSportCollection: ITypeSport[],
    ...typeSportsToCheck: (ITypeSport | null | undefined)[]
  ): ITypeSport[] {
    const typeSports: ITypeSport[] = typeSportsToCheck.filter(isPresent);
    if (typeSports.length > 0) {
      const typeSportCollectionIdentifiers = typeSportCollection.map(typeSportItem => getTypeSportIdentifier(typeSportItem)!);
      const typeSportsToAdd = typeSports.filter(typeSportItem => {
        const typeSportIdentifier = getTypeSportIdentifier(typeSportItem);
        if (typeSportIdentifier == null || typeSportCollectionIdentifiers.includes(typeSportIdentifier)) {
          return false;
        }
        typeSportCollectionIdentifiers.push(typeSportIdentifier);
        return true;
      });
      return [...typeSportsToAdd, ...typeSportCollection];
    }
    return typeSportCollection;
  }
}
