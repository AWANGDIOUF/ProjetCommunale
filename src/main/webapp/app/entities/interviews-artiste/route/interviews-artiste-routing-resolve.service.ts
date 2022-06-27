import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterviewsArtiste, InterviewsArtiste } from '../interviews-artiste.model';
import { InterviewsArtisteService } from '../service/interviews-artiste.service';

@Injectable({ providedIn: 'root' })
export class InterviewsArtisteRoutingResolveService implements Resolve<IInterviewsArtiste> {
  constructor(protected service: InterviewsArtisteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterviewsArtiste> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((interviewsArtiste: HttpResponse<InterviewsArtiste>) => {
          if (interviewsArtiste.body) {
            return of(interviewsArtiste.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InterviewsArtiste());
  }
}
