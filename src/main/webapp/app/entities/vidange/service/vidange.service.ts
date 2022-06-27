import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVidange, getVidangeIdentifier } from '../vidange.model';

export type EntityResponseType = HttpResponse<IVidange>;
export type EntityArrayResponseType = HttpResponse<IVidange[]>;

@Injectable({ providedIn: 'root' })
export class VidangeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vidanges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vidange: IVidange): Observable<EntityResponseType> {
    return this.http.post<IVidange>(this.resourceUrl, vidange, { observe: 'response' });
  }

  update(vidange: IVidange): Observable<EntityResponseType> {
    return this.http.put<IVidange>(`${this.resourceUrl}/${getVidangeIdentifier(vidange) as number}`, vidange, { observe: 'response' });
  }

  partialUpdate(vidange: IVidange): Observable<EntityResponseType> {
    return this.http.patch<IVidange>(`${this.resourceUrl}/${getVidangeIdentifier(vidange) as number}`, vidange, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVidange>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVidange[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVidangeToCollectionIfMissing(vidangeCollection: IVidange[], ...vidangesToCheck: (IVidange | null | undefined)[]): IVidange[] {
    const vidanges: IVidange[] = vidangesToCheck.filter(isPresent);
    if (vidanges.length > 0) {
      const vidangeCollectionIdentifiers = vidangeCollection.map(vidangeItem => getVidangeIdentifier(vidangeItem)!);
      const vidangesToAdd = vidanges.filter(vidangeItem => {
        const vidangeIdentifier = getVidangeIdentifier(vidangeItem);
        if (vidangeIdentifier == null || vidangeCollectionIdentifiers.includes(vidangeIdentifier)) {
          return false;
        }
        vidangeCollectionIdentifiers.push(vidangeIdentifier);
        return true;
      });
      return [...vidangesToAdd, ...vidangeCollection];
    }
    return vidangeCollection;
  }
}
