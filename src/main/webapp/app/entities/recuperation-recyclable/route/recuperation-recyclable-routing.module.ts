import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecuperationRecyclableComponent } from '../list/recuperation-recyclable.component';
import { RecuperationRecyclableDetailComponent } from '../detail/recuperation-recyclable-detail.component';
import { RecuperationRecyclableUpdateComponent } from '../update/recuperation-recyclable-update.component';
import { RecuperationRecyclableRoutingResolveService } from './recuperation-recyclable-routing-resolve.service';

const recuperationRecyclableRoute: Routes = [
  {
    path: '',
    component: RecuperationRecyclableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecuperationRecyclableDetailComponent,
    resolve: {
      recuperationRecyclable: RecuperationRecyclableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecuperationRecyclableUpdateComponent,
    resolve: {
      recuperationRecyclable: RecuperationRecyclableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecuperationRecyclableUpdateComponent,
    resolve: {
      recuperationRecyclable: RecuperationRecyclableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recuperationRecyclableRoute)],
  exports: [RouterModule],
})
export class RecuperationRecyclableRoutingModule {}
