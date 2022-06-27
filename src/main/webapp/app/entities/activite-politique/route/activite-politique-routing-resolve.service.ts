import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActivitePolitique, ActivitePolitique } from '../activite-politique.model';
import { ActivitePolitiqueService } from '../service/activite-politique.service';

@Injectable({ providedIn: 'root' })
export class ActivitePolitiqueRoutingResolveService implements Resolve<IActivitePolitique> {
  constructor(protected service: ActivitePolitiqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActivitePolitique> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((activitePolitique: HttpResponse<ActivitePolitique>) => {
          if (activitePolitique.body) {
            return of(activitePolitique.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ActivitePolitique());
  }
}
