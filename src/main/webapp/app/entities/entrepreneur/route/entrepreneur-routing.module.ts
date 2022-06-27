import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EntrepreneurComponent } from '../list/entrepreneur.component';
import { EntrepreneurDetailComponent } from '../detail/entrepreneur-detail.component';
import { EntrepreneurUpdateComponent } from '../update/entrepreneur-update.component';
import { EntrepreneurRoutingResolveService } from './entrepreneur-routing-resolve.service';

const entrepreneurRoute: Routes = [
  {
    path: '',
    component: EntrepreneurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntrepreneurDetailComponent,
    resolve: {
      entrepreneur: EntrepreneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntrepreneurUpdateComponent,
    resolve: {
      entrepreneur: EntrepreneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntrepreneurUpdateComponent,
    resolve: {
      entrepreneur: EntrepreneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(entrepreneurRoute)],
  exports: [RouterModule],
})
export class EntrepreneurRoutingModule {}
