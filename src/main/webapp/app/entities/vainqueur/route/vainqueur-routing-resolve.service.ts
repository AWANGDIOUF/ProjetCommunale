import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVainqueur, Vainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';

@Injectable({ providedIn: 'root' })
export class VainqueurRoutingResolveService implements Resolve<IVainqueur> {
  constructor(protected service: VainqueurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVainqueur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vainqueur: HttpResponse<Vainqueur>) => {
          if (vainqueur.body) {
            return of(vainqueur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vainqueur());
  }
}
