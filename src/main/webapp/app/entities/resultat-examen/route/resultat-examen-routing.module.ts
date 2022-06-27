import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResultatExamenComponent } from '../list/resultat-examen.component';
import { ResultatExamenDetailComponent } from '../detail/resultat-examen-detail.component';
import { ResultatExamenUpdateComponent } from '../update/resultat-examen-update.component';
import { ResultatExamenRoutingResolveService } from './resultat-examen-routing-resolve.service';

const resultatExamenRoute: Routes = [
  {
    path: '',
    component: ResultatExamenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResultatExamenDetailComponent,
    resolve: {
      resultatExamen: ResultatExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResultatExamenUpdateComponent,
    resolve: {
      resultatExamen: ResultatExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResultatExamenUpdateComponent,
    resolve: {
      resultatExamen: ResultatExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resultatExamenRoute)],
  exports: [RouterModule],
})
export class ResultatExamenRoutingModule {}
