import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArtiste, getArtisteIdentifier } from '../artiste.model';

export type EntityResponseType = HttpResponse<IArtiste>;
export type EntityArrayResponseType = HttpResponse<IArtiste[]>;

@Injectable({ providedIn: 'root' })
export class ArtisteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/artistes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(artiste: IArtiste): Observable<EntityResponseType> {
    return this.http.post<IArtiste>(this.resourceUrl, artiste, { observe: 'response' });
  }

  update(artiste: IArtiste): Observable<EntityResponseType> {
    return this.http.put<IArtiste>(`${this.resourceUrl}/${getArtisteIdentifier(artiste) as number}`, artiste, { observe: 'response' });
  }

  partialUpdate(artiste: IArtiste): Observable<EntityResponseType> {
    return this.http.patch<IArtiste>(`${this.resourceUrl}/${getArtisteIdentifier(artiste) as number}`, artiste, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArtiste>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArtiste[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addArtisteToCollectionIfMissing(artisteCollection: IArtiste[], ...artistesToCheck: (IArtiste | null | undefined)[]): IArtiste[] {
    const artistes: IArtiste[] = artistesToCheck.filter(isPresent);
    if (artistes.length > 0) {
      const artisteCollectionIdentifiers = artisteCollection.map(artisteItem => getArtisteIdentifier(artisteItem)!);
      const artistesToAdd = artistes.filter(artisteItem => {
        const artisteIdentifier = getArtisteIdentifier(artisteItem);
        if (artisteIdentifier == null || artisteCollectionIdentifiers.includes(artisteIdentifier)) {
          return false;
        }
        artisteCollectionIdentifiers.push(artisteIdentifier);
        return true;
      });
      return [...artistesToAdd, ...artisteCollection];
    }
    return artisteCollection;
  }
}
