import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensibiisationInternetComponent } from '../list/sensibiisation-internet.component';
import { SensibiisationInternetDetailComponent } from '../detail/sensibiisation-internet-detail.component';
import { SensibiisationInternetUpdateComponent } from '../update/sensibiisation-internet-update.component';
import { SensibiisationInternetRoutingResolveService } from './sensibiisation-internet-routing-resolve.service';

const sensibiisationInternetRoute: Routes = [
  {
    path: '',
    component: SensibiisationInternetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensibiisationInternetDetailComponent,
    resolve: {
      sensibiisationInternet: SensibiisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensibiisationInternetUpdateComponent,
    resolve: {
      sensibiisationInternet: SensibiisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensibiisationInternetUpdateComponent,
    resolve: {
      sensibiisationInternet: SensibiisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sensibiisationInternetRoute)],
  exports: [RouterModule],
})
export class SensibiisationInternetRoutingModule {}
