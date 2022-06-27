import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeSport, TypeSport } from '../type-sport.model';
import { TypeSportService } from '../service/type-sport.service';

@Injectable({ providedIn: 'root' })
export class TypeSportRoutingResolveService implements Resolve<ITypeSport> {
  constructor(protected service: TypeSportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeSport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeSport: HttpResponse<TypeSport>) => {
          if (typeSport.body) {
            return of(typeSport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeSport());
  }
}
