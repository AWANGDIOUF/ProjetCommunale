import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonSangComponent } from './list/don-sang.component';
import { DonSangDetailComponent } from './detail/don-sang-detail.component';
import { DonSangUpdateComponent } from './update/don-sang-update.component';
import { DonSangDeleteDialogComponent } from './delete/don-sang-delete-dialog.component';
import { DonSangRoutingModule } from './route/don-sang-routing.module';

@NgModule({
  imports: [SharedModule, DonSangRoutingModule],
  declarations: [DonSangComponent, DonSangDetailComponent, DonSangUpdateComponent, DonSangDeleteDialogComponent],
  entryComponents: [DonSangDeleteDialogComponent],
})
export class DonSangModule {}
