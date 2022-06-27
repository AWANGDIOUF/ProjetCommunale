import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArchiveSportComponent } from '../list/archive-sport.component';
import { ArchiveSportDetailComponent } from '../detail/archive-sport-detail.component';
import { ArchiveSportUpdateComponent } from '../update/archive-sport-update.component';
import { ArchiveSportRoutingResolveService } from './archive-sport-routing-resolve.service';

const archiveSportRoute: Routes = [
  {
    path: '',
    component: ArchiveSportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArchiveSportDetailComponent,
    resolve: {
      archiveSport: ArchiveSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArchiveSportUpdateComponent,
    resolve: {
      archiveSport: ArchiveSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArchiveSportUpdateComponent,
    resolve: {
      archiveSport: ArchiveSportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(archiveSportRoute)],
  exports: [RouterModule],
})
export class ArchiveSportRoutingModule {}
