import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResultatExamen, ResultatExamen } from '../resultat-examen.model';
import { ResultatExamenService } from '../service/resultat-examen.service';

@Injectable({ providedIn: 'root' })
export class ResultatExamenRoutingResolveService implements Resolve<IResultatExamen> {
  constructor(protected service: ResultatExamenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResultatExamen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resultatExamen: HttpResponse<ResultatExamen>) => {
          if (resultatExamen.body) {
            return of(resultatExamen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResultatExamen());
  }
}
