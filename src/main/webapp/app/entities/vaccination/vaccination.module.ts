import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VaccinationComponent } from './list/vaccination.component';
import { VaccinationDetailComponent } from './detail/vaccination-detail.component';
import { VaccinationUpdateComponent } from './update/vaccination-update.component';
import { VaccinationDeleteDialogComponent } from './delete/vaccination-delete-dialog.component';
import { VaccinationRoutingModule } from './route/vaccination-routing.module';

@NgModule({
  imports: [SharedModule, VaccinationRoutingModule],
  declarations: [VaccinationComponent, VaccinationDetailComponent, VaccinationUpdateComponent, VaccinationDeleteDialogComponent],
  entryComponents: [VaccinationDeleteDialogComponent],
})
export class VaccinationModule {}
