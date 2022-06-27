import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CombattantComponent } from '../list/combattant.component';
import { CombattantDetailComponent } from '../detail/combattant-detail.component';
import { CombattantUpdateComponent } from '../update/combattant-update.component';
import { CombattantRoutingResolveService } from './combattant-routing-resolve.service';

const combattantRoute: Routes = [
  {
    path: '',
    component: CombattantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CombattantDetailComponent,
    resolve: {
      combattant: CombattantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CombattantUpdateComponent,
    resolve: {
      combattant: CombattantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CombattantUpdateComponent,
    resolve: {
      combattant: CombattantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(combattantRoute)],
  exports: [RouterModule],
})
export class CombattantRoutingModule {}
