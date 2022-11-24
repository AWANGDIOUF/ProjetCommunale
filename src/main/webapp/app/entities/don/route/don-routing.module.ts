import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DonComponent } from '../list/don.component';
import { DonDetailComponent } from '../detail/don-detail.component';
import { DonUpdateComponent } from '../update/don-update.component';
import { DonRoutingResolveService } from './don-routing-resolve.service';

const donRoute: Routes = [
  {
    path: '',
    component: DonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonDetailComponent,
    resolve: {
      don: DonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonUpdateComponent,
    resolve: {
      don: DonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonUpdateComponent,
    resolve: {
      don: DonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donRoute)],
  exports: [RouterModule],
})
export class DonRoutingModule {}
