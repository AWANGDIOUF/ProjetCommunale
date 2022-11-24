import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonSang, DonSang } from '../don-sang.model';
import { DonSangService } from '../service/don-sang.service';

@Injectable({ providedIn: 'root' })
export class DonSangRoutingResolveService implements Resolve<IDonSang> {
  constructor(protected service: DonSangService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonSang> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((donSang: HttpResponse<DonSang>) => {
          if (donSang.body) {
            return of(donSang.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DonSang());
  }
}
