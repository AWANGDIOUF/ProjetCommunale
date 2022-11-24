import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVaccination, Vaccination } from '../vaccination.model';
import { VaccinationService } from '../service/vaccination.service';

@Injectable({ providedIn: 'root' })
export class VaccinationRoutingResolveService implements Resolve<IVaccination> {
  constructor(protected service: VaccinationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVaccination> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vaccination: HttpResponse<Vaccination>) => {
          if (vaccination.body) {
            return of(vaccination.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vaccination());
  }
}
