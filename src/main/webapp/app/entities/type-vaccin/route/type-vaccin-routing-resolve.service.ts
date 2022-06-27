import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';

@Injectable({ providedIn: 'root' })
export class TypeVaccinRoutingResolveService implements Resolve<ITypeVaccin> {
  constructor(protected service: TypeVaccinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeVaccin> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeVaccin: HttpResponse<TypeVaccin>) => {
          if (typeVaccin.body) {
            return of(typeVaccin.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeVaccin());
  }
}
