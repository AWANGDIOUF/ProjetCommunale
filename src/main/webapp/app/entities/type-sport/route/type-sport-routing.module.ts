import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeSportComponent } from '../list/type-sport.component';
import { TypeSportDetailComponent } from '../detail/type-sport-detail.component';
import { TypeSportUpdateComponent } from '../update/type-sport-update.component';
import { TypeSportRoutingResolveService } from './type-sport-routing-resolve.service';

const typeSportRoute: Routes = [
  {
    path: '',
    component: TypeSportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeSportDetailComponent,
    resolve: {
      typeSport: TypeSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeSportUpdateComponent,
    resolve: {
      typeSport: TypeSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeSportUpdateComponent,
    resolve: {
      typeSport: TypeSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeSportRoute)],
  exports: [RouterModule],
})
export class TypeSportRoutingModule {}
