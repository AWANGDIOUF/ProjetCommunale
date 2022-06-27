import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatch, getMatchIdentifier } from '../match.model';

export type EntityResponseType = HttpResponse<IMatch>;
export type EntityArrayResponseType = HttpResponse<IMatch[]>;

@Injectable({ providedIn: 'root' })
export class MatchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/matches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(match: IMatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(match);
    return this.http
      .post<IMatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(match: IMatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(match);
    return this.http
      .put<IMatch>(`${this.resourceUrl}/${getMatchIdentifier(match) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(match: IMatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(match);
    return this.http
      .patch<IMatch>(`${this.resourceUrl}/${getMatchIdentifier(match) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchToCollectionIfMissing(matchCollection: IMatch[], ...matchesToCheck: (IMatch | null | undefined)[]): IMatch[] {
    const matches: IMatch[] = matchesToCheck.filter(isPresent);
    if (matches.length > 0) {
      const matchCollectionIdentifiers = matchCollection.map(matchItem => getMatchIdentifier(matchItem)!);
      const matchesToAdd = matches.filter(matchItem => {
        const matchIdentifier = getMatchIdentifier(matchItem);
        if (matchIdentifier == null || matchCollectionIdentifiers.includes(matchIdentifier)) {
          return false;
        }
        matchCollectionIdentifiers.push(matchIdentifier);
        return true;
      });
      return [...matchesToAdd, ...matchCollection];
    }
    return matchCollection;
  }

  protected convertDateFromClient(match: IMatch): IMatch {
    return Object.assign({}, match, {
      dateMatch: match.dateMatch?.isValid() ? match.dateMatch.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateMatch = res.body.dateMatch ? dayjs(res.body.dateMatch) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((match: IMatch) => {
        match.dateMatch = match.dateMatch ? dayjs(match.dateMatch) : undefined;
      });
    }
    return res;
  }
}
