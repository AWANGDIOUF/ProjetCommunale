import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonComponent } from './list/don.component';
import { DonDetailComponent } from './detail/don-detail.component';
import { DonUpdateComponent } from './update/don-update.component';
import { DonDeleteDialogComponent } from './delete/don-delete-dialog.component';
import { DonRoutingModule } from './route/don-routing.module';

@NgModule({
  imports: [SharedModule, DonRoutingModule],
  declarations: [DonComponent, DonDetailComponent, DonUpdateComponent, DonDeleteDialogComponent],
  entryComponents: [DonDeleteDialogComponent],
})
export class DonModule {}
