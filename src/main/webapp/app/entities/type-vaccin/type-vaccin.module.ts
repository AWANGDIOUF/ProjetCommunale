import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeVaccinComponent } from './list/type-vaccin.component';
import { TypeVaccinDetailComponent } from './detail/type-vaccin-detail.component';
import { TypeVaccinUpdateComponent } from './update/type-vaccin-update.component';
import { TypeVaccinDeleteDialogComponent } from './delete/type-vaccin-delete-dialog.component';
import { TypeVaccinRoutingModule } from './route/type-vaccin-routing.module';

@NgModule({
  imports: [SharedModule, TypeVaccinRoutingModule],
  declarations: [TypeVaccinComponent, TypeVaccinDetailComponent, TypeVaccinUpdateComponent, TypeVaccinDeleteDialogComponent],
  entryComponents: [TypeVaccinDeleteDialogComponent],
})
export class TypeVaccinModule {}
