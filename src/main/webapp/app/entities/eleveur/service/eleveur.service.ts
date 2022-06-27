import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEleveur, getEleveurIdentifier } from '../eleveur.model';

export type EntityResponseType = HttpResponse<IEleveur>;
export type EntityArrayResponseType = HttpResponse<IEleveur[]>;

@Injectable({ providedIn: 'root' })
export class EleveurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eleveurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eleveur: IEleveur): Observable<EntityResponseType> {
    return this.http.post<IEleveur>(this.resourceUrl, eleveur, { observe: 'response' });
  }

  update(eleveur: IEleveur): Observable<EntityResponseType> {
    return this.http.put<IEleveur>(`${this.resourceUrl}/${getEleveurIdentifier(eleveur) as number}`, eleveur, { observe: 'response' });
  }

  partialUpdate(eleveur: IEleveur): Observable<EntityResponseType> {
    return this.http.patch<IEleveur>(`${this.resourceUrl}/${getEleveurIdentifier(eleveur) as number}`, eleveur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEleveur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEleveur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEleveurToCollectionIfMissing(eleveurCollection: IEleveur[], ...eleveursToCheck: (IEleveur | null | undefined)[]): IEleveur[] {
    const eleveurs: IEleveur[] = eleveursToCheck.filter(isPresent);
    if (eleveurs.length > 0) {
      const eleveurCollectionIdentifiers = eleveurCollection.map(eleveurItem => getEleveurIdentifier(eleveurItem)!);
      const eleveursToAdd = eleveurs.filter(eleveurItem => {
        const eleveurIdentifier = getEleveurIdentifier(eleveurItem);
        if (eleveurIdentifier == null || eleveurCollectionIdentifiers.includes(eleveurIdentifier)) {
          return false;
        }
        eleveurCollectionIdentifiers.push(eleveurIdentifier);
        return true;
      });
      return [...eleveursToAdd, ...eleveurCollection];
    }
    return eleveurCollection;
  }
}
