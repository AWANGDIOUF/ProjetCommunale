import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArtisteComponent } from '../list/artiste.component';
import { ArtisteDetailComponent } from '../detail/artiste-detail.component';
import { ArtisteUpdateComponent } from '../update/artiste-update.component';
import { ArtisteRoutingResolveService } from './artiste-routing-resolve.service';

const artisteRoute: Routes = [
  {
    path: '',
    component: ArtisteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArtisteDetailComponent,
    resolve: {
      artiste: ArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArtisteUpdateComponent,
    resolve: {
      artiste: ArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArtisteUpdateComponent,
    resolve: {
      artiste: ArtisteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(artisteRoute)],
  exports: [RouterModule],
})
export class ArtisteRoutingModule {}
