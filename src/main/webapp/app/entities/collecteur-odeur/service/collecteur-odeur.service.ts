import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICollecteurOdeur, getCollecteurOdeurIdentifier } from '../collecteur-odeur.model';

export type EntityResponseType = HttpResponse<ICollecteurOdeur>;
export type EntityArrayResponseType = HttpResponse<ICollecteurOdeur[]>;

@Injectable({ providedIn: 'root' })
export class CollecteurOdeurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/collecteur-odeurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(collecteurOdeur: ICollecteurOdeur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collecteurOdeur);
    return this.http
      .post<ICollecteurOdeur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(collecteurOdeur: ICollecteurOdeur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collecteurOdeur);
    return this.http
      .put<ICollecteurOdeur>(`${this.resourceUrl}/${getCollecteurOdeurIdentifier(collecteurOdeur) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(collecteurOdeur: ICollecteurOdeur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collecteurOdeur);
    return this.http
      .patch<ICollecteurOdeur>(`${this.resourceUrl}/${getCollecteurOdeurIdentifier(collecteurOdeur) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICollecteurOdeur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICollecteurOdeur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCollecteurOdeurToCollectionIfMissing(
    collecteurOdeurCollection: ICollecteurOdeur[],
    ...collecteurOdeursToCheck: (ICollecteurOdeur | null | undefined)[]
  ): ICollecteurOdeur[] {
    const collecteurOdeurs: ICollecteurOdeur[] = collecteurOdeursToCheck.filter(isPresent);
    if (collecteurOdeurs.length > 0) {
      const collecteurOdeurCollectionIdentifiers = collecteurOdeurCollection.map(
        collecteurOdeurItem => getCollecteurOdeurIdentifier(collecteurOdeurItem)!
      );
      const collecteurOdeursToAdd = collecteurOdeurs.filter(collecteurOdeurItem => {
        const collecteurOdeurIdentifier = getCollecteurOdeurIdentifier(collecteurOdeurItem);
        if (collecteurOdeurIdentifier == null || collecteurOdeurCollectionIdentifiers.includes(collecteurOdeurIdentifier)) {
          return false;
        }
        collecteurOdeurCollectionIdentifiers.push(collecteurOdeurIdentifier);
        return true;
      });
      return [...collecteurOdeursToAdd, ...collecteurOdeurCollection];
    }
    return collecteurOdeurCollection;
  }

  protected convertDateFromClient(collecteurOdeur: ICollecteurOdeur): ICollecteurOdeur {
    return Object.assign({}, collecteurOdeur, {
      date: collecteurOdeur.date?.isValid() ? collecteurOdeur.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((collecteurOdeur: ICollecteurOdeur) => {
        collecteurOdeur.date = collecteurOdeur.date ? dayjs(collecteurOdeur.date) : undefined;
      });
    }
    return res;
  }
}
