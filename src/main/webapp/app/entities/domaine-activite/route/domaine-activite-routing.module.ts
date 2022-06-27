import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DomaineActiviteComponent } from '../list/domaine-activite.component';
import { DomaineActiviteDetailComponent } from '../detail/domaine-activite-detail.component';
import { DomaineActiviteUpdateComponent } from '../update/domaine-activite-update.component';
import { DomaineActiviteRoutingResolveService } from './domaine-activite-routing-resolve.service';

const domaineActiviteRoute: Routes = [
  {
    path: '',
    component: DomaineActiviteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomaineActiviteDetailComponent,
    resolve: {
      domaineActivite: DomaineActiviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomaineActiviteUpdateComponent,
    resolve: {
      domaineActivite: DomaineActiviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomaineActiviteUpdateComponent,
    resolve: {
      domaineActivite: DomaineActiviteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(domaineActiviteRoute)],
  exports: [RouterModule],
})
export class DomaineActiviteRoutingModule {}
