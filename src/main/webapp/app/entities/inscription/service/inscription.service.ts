import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInscription, getInscriptionIdentifier } from '../inscription.model';

export type EntityResponseType = HttpResponse<IInscription>;
export type EntityArrayResponseType = HttpResponse<IInscription[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inscription: IInscription): Observable<EntityResponseType> {
    return this.http.post<IInscription>(this.resourceUrl, inscription, { observe: 'response' });
  }

  update(inscription: IInscription): Observable<EntityResponseType> {
    return this.http.put<IInscription>(`${this.resourceUrl}/${getInscriptionIdentifier(inscription) as number}`, inscription, {
      observe: 'response',
    });
  }

  partialUpdate(inscription: IInscription): Observable<EntityResponseType> {
    return this.http.patch<IInscription>(`${this.resourceUrl}/${getInscriptionIdentifier(inscription) as number}`, inscription, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscription>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscription[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInscriptionToCollectionIfMissing(
    inscriptionCollection: IInscription[],
    ...inscriptionsToCheck: (IInscription | null | undefined)[]
  ): IInscription[] {
    const inscriptions: IInscription[] = inscriptionsToCheck.filter(isPresent);
    if (inscriptions.length > 0) {
      const inscriptionCollectionIdentifiers = inscriptionCollection.map(inscriptionItem => getInscriptionIdentifier(inscriptionItem)!);
      const inscriptionsToAdd = inscriptions.filter(inscriptionItem => {
        const inscriptionIdentifier = getInscriptionIdentifier(inscriptionItem);
        if (inscriptionIdentifier == null || inscriptionCollectionIdentifiers.includes(inscriptionIdentifier)) {
          return false;
        }
        inscriptionCollectionIdentifiers.push(inscriptionIdentifier);
        return true;
      });
      return [...inscriptionsToAdd, ...inscriptionCollection];
    }
    return inscriptionCollection;
  }
}
