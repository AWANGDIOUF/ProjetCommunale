import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { BeneficiaireService } from '../service/beneficiaire.service';

@Injectable({ providedIn: 'root' })
export class BeneficiaireRoutingResolveService implements Resolve<IBeneficiaire> {
  constructor(protected service: BeneficiaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBeneficiaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((beneficiaire: HttpResponse<Beneficiaire>) => {
          if (beneficiaire.body) {
            return of(beneficiaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Beneficiaire());
  }
}
