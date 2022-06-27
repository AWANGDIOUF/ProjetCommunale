import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArchiveSport, getArchiveSportIdentifier } from '../archive-sport.model';

export type EntityResponseType = HttpResponse<IArchiveSport>;
export type EntityArrayResponseType = HttpResponse<IArchiveSport[]>;

@Injectable({ providedIn: 'root' })
export class ArchiveSportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/archive-sports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(archiveSport: IArchiveSport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveSport);
    return this.http
      .post<IArchiveSport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(archiveSport: IArchiveSport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveSport);
    return this.http
      .put<IArchiveSport>(`${this.resourceUrl}/${getArchiveSportIdentifier(archiveSport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(archiveSport: IArchiveSport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveSport);
    return this.http
      .patch<IArchiveSport>(`${this.resourceUrl}/${getArchiveSportIdentifier(archiveSport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArchiveSport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IArchiveSport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addArchiveSportToCollectionIfMissing(
    archiveSportCollection: IArchiveSport[],
    ...archiveSportsToCheck: (IArchiveSport | null | undefined)[]
  ): IArchiveSport[] {
    const archiveSports: IArchiveSport[] = archiveSportsToCheck.filter(isPresent);
    if (archiveSports.length > 0) {
      const archiveSportCollectionIdentifiers = archiveSportCollection.map(
        archiveSportItem => getArchiveSportIdentifier(archiveSportItem)!
      );
      const archiveSportsToAdd = archiveSports.filter(archiveSportItem => {
        const archiveSportIdentifier = getArchiveSportIdentifier(archiveSportItem);
        if (archiveSportIdentifier == null || archiveSportCollectionIdentifiers.includes(archiveSportIdentifier)) {
          return false;
        }
        archiveSportCollectionIdentifiers.push(archiveSportIdentifier);
        return true;
      });
      return [...archiveSportsToAdd, ...archiveSportCollection];
    }
    return archiveSportCollection;
  }

  protected convertDateFromClient(archiveSport: IArchiveSport): IArchiveSport {
    return Object.assign({}, archiveSport, {
      annee: archiveSport.annee?.isValid() ? archiveSport.annee.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.annee = res.body.annee ? dayjs(res.body.annee) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((archiveSport: IArchiveSport) => {
        archiveSport.annee = archiveSport.annee ? dayjs(archiveSport.annee) : undefined;
      });
    }
    return res;
  }
}
