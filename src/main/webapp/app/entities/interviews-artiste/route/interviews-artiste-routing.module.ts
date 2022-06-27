import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterviewsArtisteComponent } from '../list/interviews-artiste.component';
import { InterviewsArtisteDetailComponent } from '../detail/interviews-artiste-detail.component';
import { InterviewsArtisteUpdateComponent } from '../update/interviews-artiste-update.component';
import { InterviewsArtisteRoutingResolveService } from './interviews-artiste-routing-resolve.service';

const interviewsArtisteRoute: Routes = [
  {
    path: '',
    component: InterviewsArtisteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterviewsArtisteDetailComponent,
    resolve: {
      interviewsArtiste: InterviewsArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterviewsArtisteUpdateComponent,
    resolve: {
      interviewsArtiste: InterviewsArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterviewsArtisteUpdateComponent,
    resolve: {
      interviewsArtiste: InterviewsArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interviewsArtisteRoute)],
  exports: [RouterModule],
})
export class InterviewsArtisteRoutingModule {}
