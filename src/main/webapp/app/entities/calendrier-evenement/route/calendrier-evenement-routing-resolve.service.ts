import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICalendrierEvenement, CalendrierEvenement } from '../calendrier-evenement.model';
import { CalendrierEvenementService } from '../service/calendrier-evenement.service';

@Injectable({ providedIn: 'root' })
export class CalendrierEvenementRoutingResolveService implements Resolve<ICalendrierEvenement> {
  constructor(protected service: CalendrierEvenementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalendrierEvenement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((calendrierEvenement: HttpResponse<CalendrierEvenement>) => {
          if (calendrierEvenement.body) {
            return of(calendrierEvenement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CalendrierEvenement());
  }
}
