import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVidange, Vidange } from '../vidange.model';
import { VidangeService } from '../service/vidange.service';

@Injectable({ providedIn: 'root' })
export class VidangeRoutingResolveService implements Resolve<IVidange> {
  constructor(protected service: VidangeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVidange> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vidange: HttpResponse<Vidange>) => {
          if (vidange.body) {
            return of(vidange.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vidange());
  }
}
