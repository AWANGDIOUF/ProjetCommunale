import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DomaineActiviteComponent } from './list/domaine-activite.component';
import { DomaineActiviteDetailComponent } from './detail/domaine-activite-detail.component';
import { DomaineActiviteUpdateComponent } from './update/domaine-activite-update.component';
import { DomaineActiviteDeleteDialogComponent } from './delete/domaine-activite-delete-dialog.component';
import { DomaineActiviteRoutingModule } from './route/domaine-activite-routing.module';

@NgModule({
  imports: [SharedModule, DomaineActiviteRoutingModule],
  declarations: [
    DomaineActiviteComponent,
    DomaineActiviteDetailComponent,
    DomaineActiviteUpdateComponent,
    DomaineActiviteDeleteDialogComponent,
  ],
  entryComponents: [DomaineActiviteDeleteDialogComponent],
})
export class DomaineActiviteModule {}
