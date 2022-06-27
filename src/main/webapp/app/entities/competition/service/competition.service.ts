import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompetition, getCompetitionIdentifier } from '../competition.model';

export type EntityResponseType = HttpResponse<ICompetition>;
export type EntityArrayResponseType = HttpResponse<ICompetition[]>;

@Injectable({ providedIn: 'root' })
export class CompetitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/competitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(competition: ICompetition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(competition);
    return this.http
      .post<ICompetition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(competition: ICompetition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(competition);
    return this.http
      .put<ICompetition>(`${this.resourceUrl}/${getCompetitionIdentifier(competition) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(competition: ICompetition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(competition);
    return this.http
      .patch<ICompetition>(`${this.resourceUrl}/${getCompetitionIdentifier(competition) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompetition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompetition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompetitionToCollectionIfMissing(
    competitionCollection: ICompetition[],
    ...competitionsToCheck: (ICompetition | null | undefined)[]
  ): ICompetition[] {
    const competitions: ICompetition[] = competitionsToCheck.filter(isPresent);
    if (competitions.length > 0) {
      const competitionCollectionIdentifiers = competitionCollection.map(competitionItem => getCompetitionIdentifier(competitionItem)!);
      const competitionsToAdd = competitions.filter(competitionItem => {
        const competitionIdentifier = getCompetitionIdentifier(competitionItem);
        if (competitionIdentifier == null || competitionCollectionIdentifiers.includes(competitionIdentifier)) {
          return false;
        }
        competitionCollectionIdentifiers.push(competitionIdentifier);
        return true;
      });
      return [...competitionsToAdd, ...competitionCollection];
    }
    return competitionCollection;
  }

  protected convertDateFromClient(competition: ICompetition): ICompetition {
    return Object.assign({}, competition, {
      dateCompetition: competition.dateCompetition?.isValid() ? competition.dateCompetition.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCompetition = res.body.dateCompetition ? dayjs(res.body.dateCompetition) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((competition: ICompetition) => {
        competition.dateCompetition = competition.dateCompetition ? dayjs(competition.dateCompetition) : undefined;
      });
    }
    return res;
  }
}
