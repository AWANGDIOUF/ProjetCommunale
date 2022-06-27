import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInterviewsArtiste, getInterviewsArtisteIdentifier } from '../interviews-artiste.model';

export type EntityResponseType = HttpResponse<IInterviewsArtiste>;
export type EntityArrayResponseType = HttpResponse<IInterviewsArtiste[]>;

@Injectable({ providedIn: 'root' })
export class InterviewsArtisteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/interviews-artistes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(interviewsArtiste: IInterviewsArtiste): Observable<EntityResponseType> {
    return this.http.post<IInterviewsArtiste>(this.resourceUrl, interviewsArtiste, { observe: 'response' });
  }

  update(interviewsArtiste: IInterviewsArtiste): Observable<EntityResponseType> {
    return this.http.put<IInterviewsArtiste>(
      `${this.resourceUrl}/${getInterviewsArtisteIdentifier(interviewsArtiste) as number}`,
      interviewsArtiste,
      { observe: 'response' }
    );
  }

  partialUpdate(interviewsArtiste: IInterviewsArtiste): Observable<EntityResponseType> {
    return this.http.patch<IInterviewsArtiste>(
      `${this.resourceUrl}/${getInterviewsArtisteIdentifier(interviewsArtiste) as number}`,
      interviewsArtiste,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterviewsArtiste>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewsArtiste[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInterviewsArtisteToCollectionIfMissing(
    interviewsArtisteCollection: IInterviewsArtiste[],
    ...interviewsArtistesToCheck: (IInterviewsArtiste | null | undefined)[]
  ): IInterviewsArtiste[] {
    const interviewsArtistes: IInterviewsArtiste[] = interviewsArtistesToCheck.filter(isPresent);
    if (interviewsArtistes.length > 0) {
      const interviewsArtisteCollectionIdentifiers = interviewsArtisteCollection.map(
        interviewsArtisteItem => getInterviewsArtisteIdentifier(interviewsArtisteItem)!
      );
      const interviewsArtistesToAdd = interviewsArtistes.filter(interviewsArtisteItem => {
        const interviewsArtisteIdentifier = getInterviewsArtisteIdentifier(interviewsArtisteItem);
        if (interviewsArtisteIdentifier == null || interviewsArtisteCollectionIdentifiers.includes(interviewsArtisteIdentifier)) {
          return false;
        }
        interviewsArtisteCollectionIdentifiers.push(interviewsArtisteIdentifier);
        return true;
      });
      return [...interviewsArtistesToAdd, ...interviewsArtisteCollection];
    }
    return interviewsArtisteCollection;
  }
}
