import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipe, getEquipeIdentifier } from '../equipe.model';

export type EntityResponseType = HttpResponse<IEquipe>;
export type EntityArrayResponseType = HttpResponse<IEquipe[]>;

@Injectable({ providedIn: 'root' })
export class EquipeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/equipes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(equipe: IEquipe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(equipe);
    return this.http
      .post<IEquipe>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(equipe: IEquipe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(equipe);
    return this.http
      .put<IEquipe>(`${this.resourceUrl}/${getEquipeIdentifier(equipe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(equipe: IEquipe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(equipe);
    return this.http
      .patch<IEquipe>(`${this.resourceUrl}/${getEquipeIdentifier(equipe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEquipe>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEquipe[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEquipeToCollectionIfMissing(equipeCollection: IEquipe[], ...equipesToCheck: (IEquipe | null | undefined)[]): IEquipe[] {
    const equipes: IEquipe[] = equipesToCheck.filter(isPresent);
    if (equipes.length > 0) {
      const equipeCollectionIdentifiers = equipeCollection.map(equipeItem => getEquipeIdentifier(equipeItem)!);
      const equipesToAdd = equipes.filter(equipeItem => {
        const equipeIdentifier = getEquipeIdentifier(equipeItem);
        if (equipeIdentifier == null || equipeCollectionIdentifiers.includes(equipeIdentifier)) {
          return false;
        }
        equipeCollectionIdentifiers.push(equipeIdentifier);
        return true;
      });
      return [...equipesToAdd, ...equipeCollection];
    }
    return equipeCollection;
  }

  protected convertDateFromClient(equipe: IEquipe): IEquipe {
    return Object.assign({}, equipe, {
      dateCreation: equipe.dateCreation?.isValid() ? equipe.dateCreation.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((equipe: IEquipe) => {
        equipe.dateCreation = equipe.dateCreation ? dayjs(equipe.dateCreation) : undefined;
      });
    }
    return res;
  }
}
