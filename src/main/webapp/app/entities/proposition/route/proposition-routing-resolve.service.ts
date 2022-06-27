import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProposition, Proposition } from '../proposition.model';
import { PropositionService } from '../service/proposition.service';

@Injectable({ providedIn: 'root' })
export class PropositionRoutingResolveService implements Resolve<IProposition> {
  constructor(protected service: PropositionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProposition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((proposition: HttpResponse<Proposition>) => {
          if (proposition.body) {
            return of(proposition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Proposition());
  }
}
