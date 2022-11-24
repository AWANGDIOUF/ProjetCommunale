import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CibleComponent } from '../list/cible.component';
import { CibleDetailComponent } from '../detail/cible-detail.component';
import { CibleUpdateComponent } from '../update/cible-update.component';
import { CibleRoutingResolveService } from './cible-routing-resolve.service';

const cibleRoute: Routes = [
  {
    path: '',
    component: CibleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CibleDetailComponent,
    resolve: {
      cible: CibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CibleUpdateComponent,
    resolve: {
      cible: CibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CibleUpdateComponent,
    resolve: {
      cible: CibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cibleRoute)],
  exports: [RouterModule],
})
export class CibleRoutingModule {}
