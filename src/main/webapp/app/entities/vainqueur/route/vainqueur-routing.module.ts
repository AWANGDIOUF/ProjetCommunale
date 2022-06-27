import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VainqueurComponent } from '../list/vainqueur.component';
import { VainqueurDetailComponent } from '../detail/vainqueur-detail.component';
import { VainqueurUpdateComponent } from '../update/vainqueur-update.component';
import { VainqueurRoutingResolveService } from './vainqueur-routing-resolve.service';

const vainqueurRoute: Routes = [
  {
    path: '',
    component: VainqueurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VainqueurDetailComponent,
    resolve: {
      vainqueur: VainqueurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VainqueurUpdateComponent,
    resolve: {
      vainqueur: VainqueurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VainqueurUpdateComponent,
    resolve: {
      vainqueur: VainqueurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vainqueurRoute)],
  exports: [RouterModule],
})
export class VainqueurRoutingModule {}
