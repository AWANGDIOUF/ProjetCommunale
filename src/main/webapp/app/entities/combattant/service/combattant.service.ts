import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICombattant, getCombattantIdentifier } from '../combattant.model';

export type EntityResponseType = HttpResponse<ICombattant>;
export type EntityArrayResponseType = HttpResponse<ICombattant[]>;

@Injectable({ providedIn: 'root' })
export class CombattantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/combattants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(combattant: ICombattant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(combattant);
    return this.http
      .post<ICombattant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(combattant: ICombattant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(combattant);
    return this.http
      .put<ICombattant>(`${this.resourceUrl}/${getCombattantIdentifier(combattant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(combattant: ICombattant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(combattant);
    return this.http
      .patch<ICombattant>(`${this.resourceUrl}/${getCombattantIdentifier(combattant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICombattant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICombattant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCombattantToCollectionIfMissing(
    combattantCollection: ICombattant[],
    ...combattantsToCheck: (ICombattant | null | undefined)[]
  ): ICombattant[] {
    const combattants: ICombattant[] = combattantsToCheck.filter(isPresent);
    if (combattants.length > 0) {
      const combattantCollectionIdentifiers = combattantCollection.map(combattantItem => getCombattantIdentifier(combattantItem)!);
      const combattantsToAdd = combattants.filter(combattantItem => {
        const combattantIdentifier = getCombattantIdentifier(combattantItem);
        if (combattantIdentifier == null || combattantCollectionIdentifiers.includes(combattantIdentifier)) {
          return false;
        }
        combattantCollectionIdentifiers.push(combattantIdentifier);
        return true;
      });
      return [...combattantsToAdd, ...combattantCollection];
    }
    return combattantCollection;
  }

  protected convertDateFromClient(combattant: ICombattant): ICombattant {
    return Object.assign({}, combattant, {
      dateNais: combattant.dateNais?.isValid() ? combattant.dateNais.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNais = res.body.dateNais ? dayjs(res.body.dateNais) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((combattant: ICombattant) => {
        combattant.dateNais = combattant.dateNais ? dayjs(combattant.dateNais) : undefined;
      });
    }
    return res;
  }
}
