import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEntrepreneur, Entrepreneur } from '../entrepreneur.model';
import { EntrepreneurService } from '../service/entrepreneur.service';

@Injectable({ providedIn: 'root' })
export class EntrepreneurRoutingResolveService implements Resolve<IEntrepreneur> {
  constructor(protected service: EntrepreneurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntrepreneur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((entrepreneur: HttpResponse<Entrepreneur>) => {
          if (entrepreneur.body) {
            return of(entrepreneur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Entrepreneur());
  }
}
