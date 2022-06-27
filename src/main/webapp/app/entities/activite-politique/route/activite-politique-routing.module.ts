import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivitePolitiqueComponent } from '../list/activite-politique.component';
import { ActivitePolitiqueDetailComponent } from '../detail/activite-politique-detail.component';
import { ActivitePolitiqueUpdateComponent } from '../update/activite-politique-update.component';
import { ActivitePolitiqueRoutingResolveService } from './activite-politique-routing-resolve.service';

const activitePolitiqueRoute: Routes = [
  {
    path: '',
    component: ActivitePolitiqueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivitePolitiqueDetailComponent,
    resolve: {
      activitePolitique: ActivitePolitiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivitePolitiqueUpdateComponent,
    resolve: {
      activitePolitique: ActivitePolitiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivitePolitiqueUpdateComponent,
    resolve: {
      activitePolitique: ActivitePolitiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activitePolitiqueRoute)],
  exports: [RouterModule],
})
export class ActivitePolitiqueRoutingModule {}
