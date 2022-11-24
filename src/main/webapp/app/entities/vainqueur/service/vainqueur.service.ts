import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVainqueur, getVainqueurIdentifier } from '../vainqueur.model';

export type EntityResponseType = HttpResponse<IVainqueur>;
export type EntityArrayResponseType = HttpResponse<IVainqueur[]>;

@Injectable({ providedIn: 'root' })
export class VainqueurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vainqueurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vainqueur: IVainqueur): Observable<EntityResponseType> {
    return this.http.post<IVainqueur>(this.resourceUrl, vainqueur, { observe: 'response' });
  }

  update(vainqueur: IVainqueur): Observable<EntityResponseType> {
    return this.http.put<IVainqueur>(`${this.resourceUrl}/${getVainqueurIdentifier(vainqueur) as number}`, vainqueur, {
      observe: 'response',
    });
  }

  partialUpdate(vainqueur: IVainqueur): Observable<EntityResponseType> {
    return this.http.patch<IVainqueur>(`${this.resourceUrl}/${getVainqueurIdentifier(vainqueur) as number}`, vainqueur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVainqueur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVainqueur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVainqueurToCollectionIfMissing(
    vainqueurCollection: IVainqueur[],
    ...vainqueursToCheck: (IVainqueur | null | undefined)[]
  ): IVainqueur[] {
    const vainqueurs: IVainqueur[] = vainqueursToCheck.filter(isPresent);
    if (vainqueurs.length > 0) {
      const vainqueurCollectionIdentifiers = vainqueurCollection.map(vainqueurItem => getVainqueurIdentifier(vainqueurItem)!);
      const vainqueursToAdd = vainqueurs.filter(vainqueurItem => {
        const vainqueurIdentifier = getVainqueurIdentifier(vainqueurItem);
        if (vainqueurIdentifier == null || vainqueurCollectionIdentifiers.includes(vainqueurIdentifier)) {
          return false;
        }
        vainqueurCollectionIdentifiers.push(vainqueurIdentifier);
        return true;
      });
      return [...vainqueursToAdd, ...vainqueurCollection];
    }
    return vainqueurCollection;
  }
}
