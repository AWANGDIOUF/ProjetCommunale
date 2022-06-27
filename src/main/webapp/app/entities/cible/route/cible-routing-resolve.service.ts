import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICible, Cible } from '../cible.model';
import { CibleService } from '../service/cible.service';

@Injectable({ providedIn: 'root' })
export class CibleRoutingResolveService implements Resolve<ICible> {
  constructor(protected service: CibleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICible> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cible: HttpResponse<Cible>) => {
          if (cible.body) {
            return of(cible.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cible());
  }
}
