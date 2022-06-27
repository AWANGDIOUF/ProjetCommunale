import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EntreprenariatComponent } from '../list/entreprenariat.component';
import { EntreprenariatDetailComponent } from '../detail/entreprenariat-detail.component';
import { EntreprenariatUpdateComponent } from '../update/entreprenariat-update.component';
import { EntreprenariatRoutingResolveService } from './entreprenariat-routing-resolve.service';

const entreprenariatRoute: Routes = [
  {
    path: '',
    component: EntreprenariatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntreprenariatDetailComponent,
    resolve: {
      entreprenariat: EntreprenariatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntreprenariatUpdateComponent,
    resolve: {
      entreprenariat: EntreprenariatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntreprenariatUpdateComponent,
    resolve: {
      entreprenariat: EntreprenariatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(entreprenariatRoute)],
  exports: [RouterModule],
})
export class EntreprenariatRoutingModule {}
