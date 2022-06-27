import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensibiisationInternetComponent } from './list/sensibiisation-internet.component';
import { SensibiisationInternetDetailComponent } from './detail/sensibiisation-internet-detail.component';
import { SensibiisationInternetUpdateComponent } from './update/sensibiisation-internet-update.component';
import { SensibiisationInternetDeleteDialogComponent } from './delete/sensibiisation-internet-delete-dialog.component';
import { SensibiisationInternetRoutingModule } from './route/sensibiisation-internet-routing.module';

@NgModule({
  imports: [SharedModule, SensibiisationInternetRoutingModule],
  declarations: [
    SensibiisationInternetComponent,
    SensibiisationInternetDetailComponent,
    SensibiisationInternetUpdateComponent,
    SensibiisationInternetDeleteDialogComponent,
  ],
  entryComponents: [SensibiisationInternetDeleteDialogComponent],
})
export class SensibiisationInternetModule {}
