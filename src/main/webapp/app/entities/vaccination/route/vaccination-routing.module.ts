import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VaccinationComponent } from '../list/vaccination.component';
import { VaccinationDetailComponent } from '../detail/vaccination-detail.component';
import { VaccinationUpdateComponent } from '../update/vaccination-update.component';
import { VaccinationRoutingResolveService } from './vaccination-routing-resolve.service';

const vaccinationRoute: Routes = [
  {
    path: '',
    component: VaccinationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VaccinationDetailComponent,
    resolve: {
      vaccination: VaccinationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VaccinationUpdateComponent,
    resolve: {
      vaccination: VaccinationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VaccinationUpdateComponent,
    resolve: {
      vaccination: VaccinationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vaccinationRoute)],
  exports: [RouterModule],
})
export class VaccinationRoutingModule {}
