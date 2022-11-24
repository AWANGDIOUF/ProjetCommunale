import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeneficiaire, getBeneficiaireIdentifier } from '../beneficiaire.model';

export type EntityResponseType = HttpResponse<IBeneficiaire>;
export type EntityArrayResponseType = HttpResponse<IBeneficiaire[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    return this.http.post<IBeneficiaire>(this.resourceUrl, beneficiaire, { observe: 'response' });
  }

  update(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    return this.http.put<IBeneficiaire>(`${this.resourceUrl}/${getBeneficiaireIdentifier(beneficiaire) as number}`, beneficiaire, {
      observe: 'response',
    });
  }

  partialUpdate(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    return this.http.patch<IBeneficiaire>(`${this.resourceUrl}/${getBeneficiaireIdentifier(beneficiaire) as number}`, beneficiaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeneficiaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeneficiaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBeneficiaireToCollectionIfMissing(
    beneficiaireCollection: IBeneficiaire[],
    ...beneficiairesToCheck: (IBeneficiaire | null | undefined)[]
  ): IBeneficiaire[] {
    const beneficiaires: IBeneficiaire[] = beneficiairesToCheck.filter(isPresent);
    if (beneficiaires.length > 0) {
      const beneficiaireCollectionIdentifiers = beneficiaireCollection.map(
        beneficiaireItem => getBeneficiaireIdentifier(beneficiaireItem)!
      );
      const beneficiairesToAdd = beneficiaires.filter(beneficiaireItem => {
        const beneficiaireIdentifier = getBeneficiaireIdentifier(beneficiaireItem);
        if (beneficiaireIdentifier == null || beneficiaireCollectionIdentifiers.includes(beneficiaireIdentifier)) {
          return false;
        }
        beneficiaireCollectionIdentifiers.push(beneficiaireIdentifier);
        return true;
      });
      return [...beneficiairesToAdd, ...beneficiaireCollection];
    }
    return beneficiaireCollection;
  }
}
