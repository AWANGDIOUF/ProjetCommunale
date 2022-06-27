import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DonSangComponent } from '../list/don-sang.component';
import { DonSangDetailComponent } from '../detail/don-sang-detail.component';
import { DonSangUpdateComponent } from '../update/don-sang-update.component';
import { DonSangRoutingResolveService } from './don-sang-routing-resolve.service';

const donSangRoute: Routes = [
  {
    path: '',
    component: DonSangComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonSangDetailComponent,
    resolve: {
      donSang: DonSangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonSangUpdateComponent,
    resolve: {
      donSang: DonSangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonSangUpdateComponent,
    resolve: {
      donSang: DonSangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donSangRoute)],
  exports: [RouterModule],
})
export class DonSangRoutingModule {}
