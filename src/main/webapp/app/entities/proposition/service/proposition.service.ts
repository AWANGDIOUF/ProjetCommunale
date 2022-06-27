import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProposition, getPropositionIdentifier } from '../proposition.model';

export type EntityResponseType = HttpResponse<IProposition>;
export type EntityArrayResponseType = HttpResponse<IProposition[]>;

@Injectable({ providedIn: 'root' })
export class PropositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/propositions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proposition: IProposition): Observable<EntityResponseType> {
    return this.http.post<IProposition>(this.resourceUrl, proposition, { observe: 'response' });
  }

  update(proposition: IProposition): Observable<EntityResponseType> {
    return this.http.put<IProposition>(`${this.resourceUrl}/${getPropositionIdentifier(proposition) as number}`, proposition, {
      observe: 'response',
    });
  }

  partialUpdate(proposition: IProposition): Observable<EntityResponseType> {
    return this.http.patch<IProposition>(`${this.resourceUrl}/${getPropositionIdentifier(proposition) as number}`, proposition, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProposition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProposition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPropositionToCollectionIfMissing(
    propositionCollection: IProposition[],
    ...propositionsToCheck: (IProposition | null | undefined)[]
  ): IProposition[] {
    const propositions: IProposition[] = propositionsToCheck.filter(isPresent);
    if (propositions.length > 0) {
      const propositionCollectionIdentifiers = propositionCollection.map(propositionItem => getPropositionIdentifier(propositionItem)!);
      const propositionsToAdd = propositions.filter(propositionItem => {
        const propositionIdentifier = getPropositionIdentifier(propositionItem);
        if (propositionIdentifier == null || propositionCollectionIdentifiers.includes(propositionIdentifier)) {
          return false;
        }
        propositionCollectionIdentifiers.push(propositionIdentifier);
        return true;
      });
      return [...propositionsToAdd, ...propositionCollection];
    }
    return propositionCollection;
  }
}
