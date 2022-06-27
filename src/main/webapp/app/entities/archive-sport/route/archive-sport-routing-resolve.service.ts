import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArchiveSport, ArchiveSport } from '../archive-sport.model';
import { ArchiveSportService } from '../service/archive-sport.service';

@Injectable({ providedIn: 'root' })
export class ArchiveSportRoutingResolveService implements Resolve<IArchiveSport> {
  constructor(protected service: ArchiveSportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArchiveSport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((archiveSport: HttpResponse<ArchiveSport>) => {
          if (archiveSport.body) {
            return of(archiveSport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ArchiveSport());
  }
}
