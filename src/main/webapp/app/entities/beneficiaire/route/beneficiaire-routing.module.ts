import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BeneficiaireComponent } from '../list/beneficiaire.component';
import { BeneficiaireDetailComponent } from '../detail/beneficiaire-detail.component';
import { BeneficiaireUpdateComponent } from '../update/beneficiaire-update.component';
import { BeneficiaireRoutingResolveService } from './beneficiaire-routing-resolve.service';

const beneficiaireRoute: Routes = [
  {
    path: '',
    component: BeneficiaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BeneficiaireDetailComponent,
    resolve: {
      beneficiaire: BeneficiaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BeneficiaireUpdateComponent,
    resolve: {
      beneficiaire: BeneficiaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BeneficiaireUpdateComponent,
    resolve: {
      beneficiaire: BeneficiaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(beneficiaireRoute)],
  exports: [RouterModule],
})
export class BeneficiaireRoutingModule {}
