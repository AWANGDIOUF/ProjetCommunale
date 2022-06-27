import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICollecteurOdeur, CollecteurOdeur } from '../collecteur-odeur.model';
import { CollecteurOdeurService } from '../service/collecteur-odeur.service';

@Injectable({ providedIn: 'root' })
export class CollecteurOdeurRoutingResolveService implements Resolve<ICollecteurOdeur> {
  constructor(protected service: CollecteurOdeurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollecteurOdeur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((collecteurOdeur: HttpResponse<CollecteurOdeur>) => {
          if (collecteurOdeur.body) {
            return of(collecteurOdeur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CollecteurOdeur());
  }
}
