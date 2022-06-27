import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PropositionComponent } from '../list/proposition.component';
import { PropositionDetailComponent } from '../detail/proposition-detail.component';
import { PropositionUpdateComponent } from '../update/proposition-update.component';
import { PropositionRoutingResolveService } from './proposition-routing-resolve.service';

const propositionRoute: Routes = [
  {
    path: '',
    component: PropositionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropositionDetailComponent,
    resolve: {
      proposition: PropositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropositionUpdateComponent,
    resolve: {
      proposition: PropositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropositionUpdateComponent,
    resolve: {
      proposition: PropositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(propositionRoute)],
  exports: [RouterModule],
})
export class PropositionRoutingModule {}
