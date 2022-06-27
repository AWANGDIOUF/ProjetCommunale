import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EnsegnantComponent } from '../list/ensegnant.component';
import { EnsegnantDetailComponent } from '../detail/ensegnant-detail.component';
import { EnsegnantUpdateComponent } from '../update/ensegnant-update.component';
import { EnsegnantRoutingResolveService } from './ensegnant-routing-resolve.service';

const ensegnantRoute: Routes = [
  {
    path: '',
    component: EnsegnantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnsegnantDetailComponent,
    resolve: {
      ensegnant: EnsegnantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnsegnantUpdateComponent,
    resolve: {
      ensegnant: EnsegnantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnsegnantUpdateComponent,
    resolve: {
      ensegnant: EnsegnantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ensegnantRoute)],
  exports: [RouterModule],
})
export class EnsegnantRoutingModule {}
