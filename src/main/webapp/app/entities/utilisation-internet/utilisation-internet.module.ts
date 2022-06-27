import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UtilisationInternetComponent } from './list/utilisation-internet.component';
import { UtilisationInternetDetailComponent } from './detail/utilisation-internet-detail.component';
import { UtilisationInternetUpdateComponent } from './update/utilisation-internet-update.component';
import { UtilisationInternetDeleteDialogComponent } from './delete/utilisation-internet-delete-dialog.component';
import { UtilisationInternetRoutingModule } from './route/utilisation-internet-routing.module';

@NgModule({
  imports: [SharedModule, UtilisationInternetRoutingModule],
  declarations: [
    UtilisationInternetComponent,
    UtilisationInternetDetailComponent,
    UtilisationInternetUpdateComponent,
    UtilisationInternetDeleteDialogComponent,
  ],
  entryComponents: [UtilisationInternetDeleteDialogComponent],
})
export class UtilisationInternetModule {}
