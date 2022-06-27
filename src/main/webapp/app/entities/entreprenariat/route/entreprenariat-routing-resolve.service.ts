import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEntreprenariat, Entreprenariat } from '../entreprenariat.model';
import { EntreprenariatService } from '../service/entreprenariat.service';

@Injectable({ providedIn: 'root' })
export class EntreprenariatRoutingResolveService implements Resolve<IEntreprenariat> {
  constructor(protected service: EntreprenariatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntreprenariat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((entreprenariat: HttpResponse<Entreprenariat>) => {
          if (entreprenariat.body) {
            return of(entreprenariat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Entreprenariat());
  }
}
