import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeVaccin, getTypeVaccinIdentifier } from '../type-vaccin.model';

export type EntityResponseType = HttpResponse<ITypeVaccin>;
export type EntityArrayResponseType = HttpResponse<ITypeVaccin[]>;

@Injectable({ providedIn: 'root' })
export class TypeVaccinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-vaccins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeVaccin: ITypeVaccin): Observable<EntityResponseType> {
    return this.http.post<ITypeVaccin>(this.resourceUrl, typeVaccin, { observe: 'response' });
  }

  update(typeVaccin: ITypeVaccin): Observable<EntityResponseType> {
    return this.http.put<ITypeVaccin>(`${this.resourceUrl}/${getTypeVaccinIdentifier(typeVaccin) as number}`, typeVaccin, {
      observe: 'response',
    });
  }

  partialUpdate(typeVaccin: ITypeVaccin): Observable<EntityResponseType> {
    return this.http.patch<ITypeVaccin>(`${this.resourceUrl}/${getTypeVaccinIdentifier(typeVaccin) as number}`, typeVaccin, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeVaccin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeVaccin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeVaccinToCollectionIfMissing(
    typeVaccinCollection: ITypeVaccin[],
    ...typeVaccinsToCheck: (ITypeVaccin | null | undefined)[]
  ): ITypeVaccin[] {
    const typeVaccins: ITypeVaccin[] = typeVaccinsToCheck.filter(isPresent);
    if (typeVaccins.length > 0) {
      const typeVaccinCollectionIdentifiers = typeVaccinCollection.map(typeVaccinItem => getTypeVaccinIdentifier(typeVaccinItem)!);
      const typeVaccinsToAdd = typeVaccins.filter(typeVaccinItem => {
        const typeVaccinIdentifier = getTypeVaccinIdentifier(typeVaccinItem);
        if (typeVaccinIdentifier == null || typeVaccinCollectionIdentifiers.includes(typeVaccinIdentifier)) {
          return false;
        }
        typeVaccinCollectionIdentifiers.push(typeVaccinIdentifier);
        return true;
      });
      return [...typeVaccinsToAdd, ...typeVaccinCollection];
    }
    return typeVaccinCollection;
  }
}
