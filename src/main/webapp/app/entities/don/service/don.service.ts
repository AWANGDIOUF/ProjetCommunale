import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDon, getDonIdentifier } from '../don.model';

export type EntityResponseType = HttpResponse<IDon>;
export type EntityArrayResponseType = HttpResponse<IDon[]>;

@Injectable({ providedIn: 'root' })
export class DonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(don: IDon): Observable<EntityResponseType> {
    return this.http.post<IDon>(this.resourceUrl, don, { observe: 'response' });
  }

  update(don: IDon): Observable<EntityResponseType> {
    return this.http.put<IDon>(`${this.resourceUrl}/${getDonIdentifier(don) as number}`, don, { observe: 'response' });
  }

  partialUpdate(don: IDon): Observable<EntityResponseType> {
    return this.http.patch<IDon>(`${this.resourceUrl}/${getDonIdentifier(don) as number}`, don, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDonToCollectionIfMissing(donCollection: IDon[], ...donsToCheck: (IDon | null | undefined)[]): IDon[] {
    const dons: IDon[] = donsToCheck.filter(isPresent);
    if (dons.length > 0) {
      const donCollectionIdentifiers = donCollection.map(donItem => getDonIdentifier(donItem)!);
      const donsToAdd = dons.filter(donItem => {
        const donIdentifier = getDonIdentifier(donItem);
        if (donIdentifier == null || donCollectionIdentifiers.includes(donIdentifier)) {
          return false;
        }
        donCollectionIdentifiers.push(donIdentifier);
        return true;
      });
      return [...donsToAdd, ...donCollection];
    }
    return donCollection;
  }
}
