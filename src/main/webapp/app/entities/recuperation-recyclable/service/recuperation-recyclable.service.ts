import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecuperationRecyclable, getRecuperationRecyclableIdentifier } from '../recuperation-recyclable.model';

export type EntityResponseType = HttpResponse<IRecuperationRecyclable>;
export type EntityArrayResponseType = HttpResponse<IRecuperationRecyclable[]>;

@Injectable({ providedIn: 'root' })
export class RecuperationRecyclableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recuperation-recyclables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recuperationRecyclable: IRecuperationRecyclable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recuperationRecyclable);
    return this.http
      .post<IRecuperationRecyclable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(recuperationRecyclable: IRecuperationRecyclable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recuperationRecyclable);
    return this.http
      .put<IRecuperationRecyclable>(`${this.resourceUrl}/${getRecuperationRecyclableIdentifier(recuperationRecyclable) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(recuperationRecyclable: IRecuperationRecyclable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recuperationRecyclable);
    return this.http
      .patch<IRecuperationRecyclable>(
        `${this.resourceUrl}/${getRecuperationRecyclableIdentifier(recuperationRecyclable) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecuperationRecyclable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecuperationRecyclable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecuperationRecyclableToCollectionIfMissing(
    recuperationRecyclableCollection: IRecuperationRecyclable[],
    ...recuperationRecyclablesToCheck: (IRecuperationRecyclable | null | undefined)[]
  ): IRecuperationRecyclable[] {
    const recuperationRecyclables: IRecuperationRecyclable[] = recuperationRecyclablesToCheck.filter(isPresent);
    if (recuperationRecyclables.length > 0) {
      const recuperationRecyclableCollectionIdentifiers = recuperationRecyclableCollection.map(
        recuperationRecyclableItem => getRecuperationRecyclableIdentifier(recuperationRecyclableItem)!
      );
      const recuperationRecyclablesToAdd = recuperationRecyclables.filter(recuperationRecyclableItem => {
        const recuperationRecyclableIdentifier = getRecuperationRecyclableIdentifier(recuperationRecyclableItem);
        if (
          recuperationRecyclableIdentifier == null ||
          recuperationRecyclableCollectionIdentifiers.includes(recuperationRecyclableIdentifier)
        ) {
          return false;
        }
        recuperationRecyclableCollectionIdentifiers.push(recuperationRecyclableIdentifier);
        return true;
      });
      return [...recuperationRecyclablesToAdd, ...recuperationRecyclableCollection];
    }
    return recuperationRecyclableCollection;
  }

  protected convertDateFromClient(recuperationRecyclable: IRecuperationRecyclable): IRecuperationRecyclable {
    return Object.assign({}, recuperationRecyclable, {
      date: recuperationRecyclable.date?.isValid() ? recuperationRecyclable.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((recuperationRecyclable: IRecuperationRecyclable) => {
        recuperationRecyclable.date = recuperationRecyclable.date ? dayjs(recuperationRecyclable.date) : undefined;
      });
    }
    return res;
  }
}
