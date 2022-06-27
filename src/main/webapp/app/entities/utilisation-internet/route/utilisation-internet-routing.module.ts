import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UtilisationInternetComponent } from '../list/utilisation-internet.component';
import { UtilisationInternetDetailComponent } from '../detail/utilisation-internet-detail.component';
import { UtilisationInternetUpdateComponent } from '../update/utilisation-internet-update.component';
import { UtilisationInternetRoutingResolveService } from './utilisation-internet-routing-resolve.service';

const utilisationInternetRoute: Routes = [
  {
    path: '',
    component: UtilisationInternetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilisationInternetDetailComponent,
    resolve: {
      utilisationInternet: UtilisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilisationInternetUpdateComponent,
    resolve: {
      utilisationInternet: UtilisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilisationInternetUpdateComponent,
    resolve: {
      utilisationInternet: UtilisationInternetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(utilisationInternetRoute)],
  exports: [RouterModule],
})
export class UtilisationInternetRoutingModule {}
