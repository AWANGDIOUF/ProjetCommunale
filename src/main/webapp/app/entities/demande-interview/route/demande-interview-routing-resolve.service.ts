import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeInterview, DemandeInterview } from '../demande-interview.model';
import { DemandeInterviewService } from '../service/demande-interview.service';

@Injectable({ providedIn: 'root' })
export class DemandeInterviewRoutingResolveService implements Resolve<IDemandeInterview> {
  constructor(protected service: DemandeInterviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeInterview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeInterview: HttpResponse<DemandeInterview>) => {
          if (demandeInterview.body) {
            return of(demandeInterview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeInterview());
  }
}
