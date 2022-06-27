import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeInterviewComponent } from '../list/demande-interview.component';
import { DemandeInterviewDetailComponent } from '../detail/demande-interview-detail.component';
import { DemandeInterviewUpdateComponent } from '../update/demande-interview-update.component';
import { DemandeInterviewRoutingResolveService } from './demande-interview-routing-resolve.service';

const demandeInterviewRoute: Routes = [
  {
    path: '',
    component: DemandeInterviewComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeInterviewDetailComponent,
    resolve: {
      demandeInterview: DemandeInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeInterviewUpdateComponent,
    resolve: {
      demandeInterview: DemandeInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeInterviewUpdateComponent,
    resolve: {
      demandeInterview: DemandeInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeInterviewRoute)],
  exports: [RouterModule],
})
export class DemandeInterviewRoutingModule {}
