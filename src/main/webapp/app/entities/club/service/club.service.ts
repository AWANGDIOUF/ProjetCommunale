import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClub, getClubIdentifier } from '../club.model';

export type EntityResponseType = HttpResponse<IClub>;
export type EntityArrayResponseType = HttpResponse<IClub[]>;

@Injectable({ providedIn: 'root' })
export class ClubService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clubs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(club: IClub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(club);
    return this.http
      .post<IClub>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(club: IClub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(club);
    return this.http
      .put<IClub>(`${this.resourceUrl}/${getClubIdentifier(club) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(club: IClub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(club);
    return this.http
      .patch<IClub>(`${this.resourceUrl}/${getClubIdentifier(club) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClub>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClub[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClubToCollectionIfMissing(clubCollection: IClub[], ...clubsToCheck: (IClub | null | undefined)[]): IClub[] {
    const clubs: IClub[] = clubsToCheck.filter(isPresent);
    if (clubs.length > 0) {
      const clubCollectionIdentifiers = clubCollection.map(clubItem => getClubIdentifier(clubItem)!);
      const clubsToAdd = clubs.filter(clubItem => {
        const clubIdentifier = getClubIdentifier(clubItem);
        if (clubIdentifier == null || clubCollectionIdentifiers.includes(clubIdentifier)) {
          return false;
        }
        clubCollectionIdentifiers.push(clubIdentifier);
        return true;
      });
      return [...clubsToAdd, ...clubCollection];
    }
    return clubCollection;
  }

  protected convertDateFromClient(club: IClub): IClub {
    return Object.assign({}, club, {
      dateCreation: club.dateCreation?.isValid() ? club.dateCreation.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((club: IClub) => {
        club.dateCreation = club.dateCreation ? dayjs(club.dateCreation) : undefined;
      });
    }
    return res;
  }
}
