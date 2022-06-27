import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeInterview, getDemandeInterviewIdentifier } from '../demande-interview.model';

export type EntityResponseType = HttpResponse<IDemandeInterview>;
export type EntityArrayResponseType = HttpResponse<IDemandeInterview[]>;

@Injectable({ providedIn: 'root' })
export class DemandeInterviewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-interviews');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeInterview: IDemandeInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeInterview);
    return this.http
      .post<IDemandeInterview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeInterview: IDemandeInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeInterview);
    return this.http
      .put<IDemandeInterview>(`${this.resourceUrl}/${getDemandeInterviewIdentifier(demandeInterview) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeInterview: IDemandeInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeInterview);
    return this.http
      .patch<IDemandeInterview>(`${this.resourceUrl}/${getDemandeInterviewIdentifier(demandeInterview) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeInterview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeInterview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeInterviewToCollectionIfMissing(
    demandeInterviewCollection: IDemandeInterview[],
    ...demandeInterviewsToCheck: (IDemandeInterview | null | undefined)[]
  ): IDemandeInterview[] {
    const demandeInterviews: IDemandeInterview[] = demandeInterviewsToCheck.filter(isPresent);
    if (demandeInterviews.length > 0) {
      const demandeInterviewCollectionIdentifiers = demandeInterviewCollection.map(
        demandeInterviewItem => getDemandeInterviewIdentifier(demandeInterviewItem)!
      );
      const demandeInterviewsToAdd = demandeInterviews.filter(demandeInterviewItem => {
        const demandeInterviewIdentifier = getDemandeInterviewIdentifier(demandeInterviewItem);
        if (demandeInterviewIdentifier == null || demandeInterviewCollectionIdentifiers.includes(demandeInterviewIdentifier)) {
          return false;
        }
        demandeInterviewCollectionIdentifiers.push(demandeInterviewIdentifier);
        return true;
      });
      return [...demandeInterviewsToAdd, ...demandeInterviewCollection];
    }
    return demandeInterviewCollection;
  }

  protected convertDateFromClient(demandeInterview: IDemandeInterview): IDemandeInterview {
    return Object.assign({}, demandeInterview, {
      dateInterview: demandeInterview.dateInterview?.isValid() ? demandeInterview.dateInterview.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateInterview = res.body.dateInterview ? dayjs(res.body.dateInterview) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demandeInterview: IDemandeInterview) => {
        demandeInterview.dateInterview = demandeInterview.dateInterview ? dayjs(demandeInterview.dateInterview) : undefined;
      });
    }
    return res;
  }
}
