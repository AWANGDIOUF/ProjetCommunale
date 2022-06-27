import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CalendrierEvenementComponent } from '../list/calendrier-evenement.component';
import { CalendrierEvenementDetailComponent } from '../detail/calendrier-evenement-detail.component';
import { CalendrierEvenementUpdateComponent } from '../update/calendrier-evenement-update.component';
import { CalendrierEvenementRoutingResolveService } from './calendrier-evenement-routing-resolve.service';

const calendrierEvenementRoute: Routes = [
  {
    path: '',
    component: CalendrierEvenementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CalendrierEvenementDetailComponent,
    resolve: {
      calendrierEvenement: CalendrierEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CalendrierEvenementUpdateComponent,
    resolve: {
      calendrierEvenement: CalendrierEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CalendrierEvenementUpdateComponent,
    resolve: {
      calendrierEvenement: CalendrierEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(calendrierEvenementRoute)],
  exports: [RouterModule],
})
export class CalendrierEvenementRoutingModule {}
