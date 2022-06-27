import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CollecteurOdeurComponent } from '../list/collecteur-odeur.component';
import { CollecteurOdeurDetailComponent } from '../detail/collecteur-odeur-detail.component';
import { CollecteurOdeurUpdateComponent } from '../update/collecteur-odeur-update.component';
import { CollecteurOdeurRoutingResolveService } from './collecteur-odeur-routing-resolve.service';

const collecteurOdeurRoute: Routes = [
  {
    path: '',
    component: CollecteurOdeurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CollecteurOdeurDetailComponent,
    resolve: {
      collecteurOdeur: CollecteurOdeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CollecteurOdeurUpdateComponent,
    resolve: {
      collecteurOdeur: CollecteurOdeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CollecteurOdeurUpdateComponent,
    resolve: {
      collecteurOdeur: CollecteurOdeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(collecteurOdeurRoute)],
  exports: [RouterModule],
})
export class CollecteurOdeurRoutingModule {}
