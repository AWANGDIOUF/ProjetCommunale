import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LienTutorielComponent } from '../list/lien-tutoriel.component';
import { LienTutorielDetailComponent } from '../detail/lien-tutoriel-detail.component';
import { LienTutorielUpdateComponent } from '../update/lien-tutoriel-update.component';
import { LienTutorielRoutingResolveService } from './lien-tutoriel-routing-resolve.service';

const lienTutorielRoute: Routes = [
  {
    path: '',
    component: LienTutorielComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LienTutorielDetailComponent,
    resolve: {
      lienTutoriel: LienTutorielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LienTutorielUpdateComponent,
    resolve: {
      lienTutoriel: LienTutorielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LienTutorielUpdateComponent,
    resolve: {
      lienTutoriel: LienTutorielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lienTutorielRoute)],
  exports: [RouterModule],
})
export class LienTutorielRoutingModule {}
