import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISensibiisationInternet, SensibiisationInternet } from '../sensibiisation-internet.model';
import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';

@Injectable({ providedIn: 'root' })
export class SensibiisationInternetRoutingResolveService implements Resolve<ISensibiisationInternet> {
  constructor(protected service: SensibiisationInternetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISensibiisationInternet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sensibiisationInternet: HttpResponse<SensibiisationInternet>) => {
          if (sensibiisationInternet.body) {
            return of(sensibiisationInternet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SensibiisationInternet());
  }
}
