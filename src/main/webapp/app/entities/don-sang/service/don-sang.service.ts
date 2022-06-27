import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDonSang, getDonSangIdentifier } from '../don-sang.model';

export type EntityResponseType = HttpResponse<IDonSang>;
export type EntityArrayResponseType = HttpResponse<IDonSang[]>;

@Injectable({ providedIn: 'root' })
export class DonSangService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/don-sangs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(donSang: IDonSang): Observable<EntityResponseType> {
    return this.http.post<IDonSang>(this.resourceUrl, donSang, { observe: 'response' });
  }

  update(donSang: IDonSang): Observable<EntityResponseType> {
    return this.http.put<IDonSang>(`${this.resourceUrl}/${getDonSangIdentifier(donSang) as number}`, donSang, { observe: 'response' });
  }

  partialUpdate(donSang: IDonSang): Observable<EntityResponseType> {
    return this.http.patch<IDonSang>(`${this.resourceUrl}/${getDonSangIdentifier(donSang) as number}`, donSang, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDonSang>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDonSang[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDonSangToCollectionIfMissing(donSangCollection: IDonSang[], ...donSangsToCheck: (IDonSang | null | undefined)[]): IDonSang[] {
    const donSangs: IDonSang[] = donSangsToCheck.filter(isPresent);
    if (donSangs.length > 0) {
      const donSangCollectionIdentifiers = donSangCollection.map(donSangItem => getDonSangIdentifier(donSangItem)!);
      const donSangsToAdd = donSangs.filter(donSangItem => {
        const donSangIdentifier = getDonSangIdentifier(donSangItem);
        if (donSangIdentifier == null || donSangCollectionIdentifiers.includes(donSangIdentifier)) {
          return false;
        }
        donSangCollectionIdentifiers.push(donSangIdentifier);
        return true;
      });
      return [...donSangsToAdd, ...donSangCollection];
    }
    return donSangCollection;
  }
}
