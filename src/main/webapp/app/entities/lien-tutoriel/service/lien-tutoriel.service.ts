import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILienTutoriel, getLienTutorielIdentifier } from '../lien-tutoriel.model';

export type EntityResponseType = HttpResponse<ILienTutoriel>;
export type EntityArrayResponseType = HttpResponse<ILienTutoriel[]>;

@Injectable({ providedIn: 'root' })
export class LienTutorielService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lien-tutoriels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lienTutoriel: ILienTutoriel): Observable<EntityResponseType> {
    return this.http.post<ILienTutoriel>(this.resourceUrl, lienTutoriel, { observe: 'response' });
  }

  update(lienTutoriel: ILienTutoriel): Observable<EntityResponseType> {
    return this.http.put<ILienTutoriel>(`${this.resourceUrl}/${getLienTutorielIdentifier(lienTutoriel) as number}`, lienTutoriel, {
      observe: 'response',
    });
  }

  partialUpdate(lienTutoriel: ILienTutoriel): Observable<EntityResponseType> {
    return this.http.patch<ILienTutoriel>(`${this.resourceUrl}/${getLienTutorielIdentifier(lienTutoriel) as number}`, lienTutoriel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILienTutoriel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILienTutoriel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLienTutorielToCollectionIfMissing(
    lienTutorielCollection: ILienTutoriel[],
    ...lienTutorielsToCheck: (ILienTutoriel | null | undefined)[]
  ): ILienTutoriel[] {
    const lienTutoriels: ILienTutoriel[] = lienTutorielsToCheck.filter(isPresent);
    if (lienTutoriels.length > 0) {
      const lienTutorielCollectionIdentifiers = lienTutorielCollection.map(
        lienTutorielItem => getLienTutorielIdentifier(lienTutorielItem)!
      );
      const lienTutorielsToAdd = lienTutoriels.filter(lienTutorielItem => {
        const lienTutorielIdentifier = getLienTutorielIdentifier(lienTutorielItem);
        if (lienTutorielIdentifier == null || lienTutorielCollectionIdentifiers.includes(lienTutorielIdentifier)) {
          return false;
        }
        lienTutorielCollectionIdentifiers.push(lienTutorielIdentifier);
        return true;
      });
      return [...lienTutorielsToAdd, ...lienTutorielCollection];
    }
    return lienTutorielCollection;
  }
}
