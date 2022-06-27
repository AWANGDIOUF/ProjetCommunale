import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICombattant, Combattant } from '../combattant.model';
import { CombattantService } from '../service/combattant.service';

@Injectable({ providedIn: 'root' })
export class CombattantRoutingResolveService implements Resolve<ICombattant> {
  constructor(protected service: CombattantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICombattant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((combattant: HttpResponse<Combattant>) => {
          if (combattant.body) {
            return of(combattant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Combattant());
  }
}
