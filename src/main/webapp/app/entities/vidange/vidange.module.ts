import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VidangeComponent } from './list/vidange.component';
import { VidangeDetailComponent } from './detail/vidange-detail.component';
import { VidangeUpdateComponent } from './update/vidange-update.component';
import { VidangeDeleteDialogComponent } from './delete/vidange-delete-dialog.component';
import { VidangeRoutingModule } from './route/vidange-routing.module';

@NgModule({
  imports: [SharedModule, VidangeRoutingModule],
  declarations: [VidangeComponent, VidangeDetailComponent, VidangeUpdateComponent, VidangeDeleteDialogComponent],
  entryComponents: [VidangeDeleteDialogComponent],
})
export class VidangeModule {}
