import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomaineActivite, DomaineActivite } from '../domaine-activite.model';
import { DomaineActiviteService } from '../service/domaine-activite.service';

@Injectable({ providedIn: 'root' })
export class DomaineActiviteRoutingResolveService implements Resolve<IDomaineActivite> {
  constructor(protected service: DomaineActiviteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDomaineActivite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((domaineActivite: HttpResponse<DomaineActivite>) => {
          if (domaineActivite.body) {
            return of(domaineActivite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DomaineActivite());
  }
}
