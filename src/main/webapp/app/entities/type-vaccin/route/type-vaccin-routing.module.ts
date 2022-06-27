import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeVaccinComponent } from '../list/type-vaccin.component';
import { TypeVaccinDetailComponent } from '../detail/type-vaccin-detail.component';
import { TypeVaccinUpdateComponent } from '../update/type-vaccin-update.component';
import { TypeVaccinRoutingResolveService } from './type-vaccin-routing-resolve.service';

const typeVaccinRoute: Routes = [
  {
    path: '',
    component: TypeVaccinComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeVaccinDetailComponent,
    resolve: {
      typeVaccin: TypeVaccinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeVaccinUpdateComponent,
    resolve: {
      typeVaccin: TypeVaccinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeVaccinUpdateComponent,
    resolve: {
      typeVaccin: TypeVaccinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeVaccinRoute)],
  exports: [RouterModule],
})
export class TypeVaccinRoutingModule {}
