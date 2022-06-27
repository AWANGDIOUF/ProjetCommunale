import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilisationInternet, getUtilisationInternetIdentifier } from '../utilisation-internet.model';

export type EntityResponseType = HttpResponse<IUtilisationInternet>;
export type EntityArrayResponseType = HttpResponse<IUtilisationInternet[]>;

@Injectable({ providedIn: 'root' })
export class UtilisationInternetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisation-internets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(utilisationInternet: IUtilisationInternet): Observable<EntityResponseType> {
    return this.http.post<IUtilisationInternet>(this.resourceUrl, utilisationInternet, { observe: 'response' });
  }

  update(utilisationInternet: IUtilisationInternet): Observable<EntityResponseType> {
    return this.http.put<IUtilisationInternet>(
      `${this.resourceUrl}/${getUtilisationInternetIdentifier(utilisationInternet) as number}`,
      utilisationInternet,
      { observe: 'response' }
    );
  }

  partialUpdate(utilisationInternet: IUtilisationInternet): Observable<EntityResponseType> {
    return this.http.patch<IUtilisationInternet>(
      `${this.resourceUrl}/${getUtilisationInternetIdentifier(utilisationInternet) as number}`,
      utilisationInternet,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilisationInternet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilisationInternet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUtilisationInternetToCollectionIfMissing(
    utilisationInternetCollection: IUtilisationInternet[],
    ...utilisationInternetsToCheck: (IUtilisationInternet | null | undefined)[]
  ): IUtilisationInternet[] {
    const utilisationInternets: IUtilisationInternet[] = utilisationInternetsToCheck.filter(isPresent);
    if (utilisationInternets.length > 0) {
      const utilisationInternetCollectionIdentifiers = utilisationInternetCollection.map(
        utilisationInternetItem => getUtilisationInternetIdentifier(utilisationInternetItem)!
      );
      const utilisationInternetsToAdd = utilisationInternets.filter(utilisationInternetItem => {
        const utilisationInternetIdentifier = getUtilisationInternetIdentifier(utilisationInternetItem);
        if (utilisationInternetIdentifier == null || utilisationInternetCollectionIdentifiers.includes(utilisationInternetIdentifier)) {
          return false;
        }
        utilisationInternetCollectionIdentifiers.push(utilisationInternetIdentifier);
        return true;
      });
      return [...utilisationInternetsToAdd, ...utilisationInternetCollection];
    }
    return utilisationInternetCollection;
  }
}
