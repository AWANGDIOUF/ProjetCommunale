import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnsegnant, Ensegnant } from '../ensegnant.model';
import { EnsegnantService } from '../service/ensegnant.service';

@Injectable({ providedIn: 'root' })
export class EnsegnantRoutingResolveService implements Resolve<IEnsegnant> {
  constructor(protected service: EnsegnantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnsegnant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ensegnant: HttpResponse<Ensegnant>) => {
          if (ensegnant.body) {
            return of(ensegnant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ensegnant());
  }
}
