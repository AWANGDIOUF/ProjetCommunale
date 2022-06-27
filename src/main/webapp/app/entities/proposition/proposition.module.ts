import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PropositionComponent } from './list/proposition.component';
import { PropositionDetailComponent } from './detail/proposition-detail.component';
import { PropositionUpdateComponent } from './update/proposition-update.component';
import { PropositionDeleteDialogComponent } from './delete/proposition-delete-dialog.component';
import { PropositionRoutingModule } from './route/proposition-routing.module';

@NgModule({
  imports: [SharedModule, PropositionRoutingModule],
  declarations: [PropositionComponent, PropositionDetailComponent, PropositionUpdateComponent, PropositionDeleteDialogComponent],
  entryComponents: [PropositionDeleteDialogComponent],
})
export class PropositionModule {}
