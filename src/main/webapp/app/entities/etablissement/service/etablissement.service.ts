import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtablissement, getEtablissementIdentifier } from '../etablissement.model';

export type EntityResponseType = HttpResponse<IEtablissement>;
export type EntityArrayResponseType = HttpResponse<IEtablissement[]>;

@Injectable({ providedIn: 'root' })
export class EtablissementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etablissements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(etablissement: IEtablissement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etablissement);
    return this.http
      .post<IEtablissement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(etablissement: IEtablissement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etablissement);
    return this.http
      .put<IEtablissement>(`${this.resourceUrl}/${getEtablissementIdentifier(etablissement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(etablissement: IEtablissement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etablissement);
    return this.http
      .patch<IEtablissement>(`${this.resourceUrl}/${getEtablissementIdentifier(etablissement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEtablissement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEtablissement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEtablissementToCollectionIfMissing(
    etablissementCollection: IEtablissement[],
    ...etablissementsToCheck: (IEtablissement | null | undefined)[]
  ): IEtablissement[] {
    const etablissements: IEtablissement[] = etablissementsToCheck.filter(isPresent);
    if (etablissements.length > 0) {
      const etablissementCollectionIdentifiers = etablissementCollection.map(
        etablissementItem => getEtablissementIdentifier(etablissementItem)!
      );
      const etablissementsToAdd = etablissements.filter(etablissementItem => {
        const etablissementIdentifier = getEtablissementIdentifier(etablissementItem);
        if (etablissementIdentifier == null || etablissementCollectionIdentifiers.includes(etablissementIdentifier)) {
          return false;
        }
        etablissementCollectionIdentifiers.push(etablissementIdentifier);
        return true;
      });
      return [...etablissementsToAdd, ...etablissementCollection];
    }
    return etablissementCollection;
  }

  protected convertDateFromClient(etablissement: IEtablissement): IEtablissement {
    return Object.assign({}, etablissement, {
      dateCreation: etablissement.dateCreation?.isValid() ? etablissement.dateCreation.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((etablissement: IEtablissement) => {
        etablissement.dateCreation = etablissement.dateCreation ? dayjs(etablissement.dateCreation) : undefined;
      });
    }
    return res;
  }
}
