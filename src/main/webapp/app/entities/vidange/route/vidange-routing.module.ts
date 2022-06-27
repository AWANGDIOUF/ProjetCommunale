import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VidangeComponent } from '../list/vidange.component';
import { VidangeDetailComponent } from '../detail/vidange-detail.component';
import { VidangeUpdateComponent } from '../update/vidange-update.component';
import { VidangeRoutingResolveService } from './vidange-routing-resolve.service';

const vidangeRoute: Routes = [
  {
    path: '',
    component: VidangeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VidangeDetailComponent,
    resolve: {
      vidange: VidangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VidangeUpdateComponent,
    resolve: {
      vidange: VidangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VidangeUpdateComponent,
    resolve: {
      vidange: VidangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vidangeRoute)],
  exports: [RouterModule],
})
export class VidangeRoutingModule {}
