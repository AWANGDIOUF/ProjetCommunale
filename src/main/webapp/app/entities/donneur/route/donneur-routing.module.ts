import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DonneurComponent } from '../list/donneur.component';
import { DonneurDetailComponent } from '../detail/donneur-detail.component';
import { DonneurUpdateComponent } from '../update/donneur-update.component';
import { DonneurRoutingResolveService } from './donneur-routing-resolve.service';

const donneurRoute: Routes = [
  {
    path: '',
    component: DonneurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonneurDetailComponent,
    resolve: {
      donneur: DonneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonneurUpdateComponent,
    resolve: {
      donneur: DonneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonneurUpdateComponent,
    resolve: {
      donneur: DonneurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donneurRoute)],
  exports: [RouterModule],
})
export class DonneurRoutingModule {}
