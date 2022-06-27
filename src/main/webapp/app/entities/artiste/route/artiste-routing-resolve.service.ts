import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArtiste, Artiste } from '../artiste.model';
import { ArtisteService } from '../service/artiste.service';

@Injectable({ providedIn: 'root' })
export class ArtisteRoutingResolveService implements Resolve<IArtiste> {
  constructor(protected service: ArtisteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArtiste> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((artiste: HttpResponse<Artiste>) => {
          if (artiste.body) {
            return of(artiste.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Artiste());
  }
}
