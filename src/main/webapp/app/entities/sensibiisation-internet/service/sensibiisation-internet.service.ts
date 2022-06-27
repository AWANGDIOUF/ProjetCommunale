import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensibiisationInternet, getSensibiisationInternetIdentifier } from '../sensibiisation-internet.model';

export type EntityResponseType = HttpResponse<ISensibiisationInternet>;
export type EntityArrayResponseType = HttpResponse<ISensibiisationInternet[]>;

@Injectable({ providedIn: 'root' })
export class SensibiisationInternetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensibiisation-internets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensibiisationInternet: ISensibiisationInternet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensibiisationInternet);
    return this.http
      .post<ISensibiisationInternet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sensibiisationInternet: ISensibiisationInternet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensibiisationInternet);
    return this.http
      .put<ISensibiisationInternet>(`${this.resourceUrl}/${getSensibiisationInternetIdentifier(sensibiisationInternet) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sensibiisationInternet: ISensibiisationInternet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensibiisationInternet);
    return this.http
      .patch<ISensibiisationInternet>(
        `${this.resourceUrl}/${getSensibiisationInternetIdentifier(sensibiisationInternet) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISensibiisationInternet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISensibiisationInternet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensibiisationInternetToCollectionIfMissing(
    sensibiisationInternetCollection: ISensibiisationInternet[],
    ...sensibiisationInternetsToCheck: (ISensibiisationInternet | null | undefined)[]
  ): ISensibiisationInternet[] {
    const sensibiisationInternets: ISensibiisationInternet[] = sensibiisationInternetsToCheck.filter(isPresent);
    if (sensibiisationInternets.length > 0) {
      const sensibiisationInternetCollectionIdentifiers = sensibiisationInternetCollection.map(
        sensibiisationInternetItem => getSensibiisationInternetIdentifier(sensibiisationInternetItem)!
      );
      const sensibiisationInternetsToAdd = sensibiisationInternets.filter(sensibiisationInternetItem => {
        const sensibiisationInternetIdentifier = getSensibiisationInternetIdentifier(sensibiisationInternetItem);
        if (
          sensibiisationInternetIdentifier == null ||
          sensibiisationInternetCollectionIdentifiers.includes(sensibiisationInternetIdentifier)
        ) {
          return false;
        }
        sensibiisationInternetCollectionIdentifiers.push(sensibiisationInternetIdentifier);
        return true;
      });
      return [...sensibiisationInternetsToAdd, ...sensibiisationInternetCollection];
    }
    return sensibiisationInternetCollection;
  }

  protected convertDateFromClient(sensibiisationInternet: ISensibiisationInternet): ISensibiisationInternet {
    return Object.assign({}, sensibiisationInternet, {
      theme: sensibiisationInternet.theme?.isValid() ? sensibiisationInternet.theme.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.theme = res.body.theme ? dayjs(res.body.theme) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sensibiisationInternet: ISensibiisationInternet) => {
        sensibiisationInternet.theme = sensibiisationInternet.theme ? dayjs(sensibiisationInternet.theme) : undefined;
      });
    }
    return res;
  }
}
