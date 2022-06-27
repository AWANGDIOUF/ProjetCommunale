import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
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
    const copy = this.convertDateFromClient(typeVaccin);
    return this.http
      .post<ITypeVaccin>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(typeVaccin: ITypeVaccin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(typeVaccin);
    return this.http
      .put<ITypeVaccin>(`${this.resourceUrl}/${getTypeVaccinIdentifier(typeVaccin) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(typeVaccin: ITypeVaccin): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(typeVaccin);
    return this.http
      .patch<ITypeVaccin>(`${this.resourceUrl}/${getTypeVaccinIdentifier(typeVaccin) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITypeVaccin>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeVaccin[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertDateFromClient(typeVaccin: ITypeVaccin): ITypeVaccin {
    return Object.assign({}, typeVaccin, {
      objectif: typeVaccin.objectif?.isValid() ? typeVaccin.objectif.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.objectif = res.body.objectif ? dayjs(res.body.objectif) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((typeVaccin: ITypeVaccin) => {
        typeVaccin.objectif = typeVaccin.objectif ? dayjs(typeVaccin.objectif) : undefined;
      });
    }
    return res;
  }
}
