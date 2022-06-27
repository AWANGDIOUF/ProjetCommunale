import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecuperationRecyclable, RecuperationRecyclable } from '../recuperation-recyclable.model';
import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';

@Injectable({ providedIn: 'root' })
export class RecuperationRecyclableRoutingResolveService implements Resolve<IRecuperationRecyclable> {
  constructor(protected service: RecuperationRecyclableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecuperationRecyclable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recuperationRecyclable: HttpResponse<RecuperationRecyclable>) => {
          if (recuperationRecyclable.body) {
            return of(recuperationRecyclable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecuperationRecyclable());
  }
}
