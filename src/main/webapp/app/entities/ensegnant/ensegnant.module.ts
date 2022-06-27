import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EnsegnantComponent } from './list/ensegnant.component';
import { EnsegnantDetailComponent } from './detail/ensegnant-detail.component';
import { EnsegnantUpdateComponent } from './update/ensegnant-update.component';
import { EnsegnantDeleteDialogComponent } from './delete/ensegnant-delete-dialog.component';
import { EnsegnantRoutingModule } from './route/ensegnant-routing.module';

@NgModule({
  imports: [SharedModule, EnsegnantRoutingModule],
  declarations: [EnsegnantComponent, EnsegnantDetailComponent, EnsegnantUpdateComponent, EnsegnantDeleteDialogComponent],
  entryComponents: [EnsegnantDeleteDialogComponent],
})
export class EnsegnantModule {}
