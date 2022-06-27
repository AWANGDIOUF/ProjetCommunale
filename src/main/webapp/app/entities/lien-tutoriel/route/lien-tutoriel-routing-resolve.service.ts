import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILienTutoriel, LienTutoriel } from '../lien-tutoriel.model';
import { LienTutorielService } from '../service/lien-tutoriel.service';

@Injectable({ providedIn: 'root' })
export class LienTutorielRoutingResolveService implements Resolve<ILienTutoriel> {
  constructor(protected service: LienTutorielService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILienTutoriel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lienTutoriel: HttpResponse<LienTutoriel>) => {
          if (lienTutoriel.body) {
            return of(lienTutoriel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LienTutoriel());
  }
}
