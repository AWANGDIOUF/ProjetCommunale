import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonneur, Donneur } from '../donneur.model';
import { DonneurService } from '../service/donneur.service';

@Injectable({ providedIn: 'root' })
export class DonneurRoutingResolveService implements Resolve<IDonneur> {
  constructor(protected service: DonneurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonneur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((donneur: HttpResponse<Donneur>) => {
          if (donneur.body) {
            return of(donneur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Donneur());
  }
}
