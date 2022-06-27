import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EleveurComponent } from '../list/eleveur.component';
import { EleveurDetailComponent } from '../detail/eleveur-detail.component';
import { EleveurUpdateComponent } from '../update/eleveur-update.component';
import { EleveurRoutingResolveService } from './eleveur-routing-resolve.service';

const eleveurRoute: Routes = [
  {
    path: '',
    component: EleveurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EleveurDetailComponent,
    resolve: {
      eleveur: EleveurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EleveurUpdateComponent,
    resolve: {
      eleveur: EleveurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EleveurUpdateComponent,
    resolve: {
      eleveur: EleveurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eleveurRoute)],
  exports: [RouterModule],
})
export class EleveurRoutingModule {}
