import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeSportComponent } from './list/type-sport.component';
import { TypeSportDetailComponent } from './detail/type-sport-detail.component';
import { TypeSportUpdateComponent } from './update/type-sport-update.component';
import { TypeSportDeleteDialogComponent } from './delete/type-sport-delete-dialog.component';
import { TypeSportRoutingModule } from './route/type-sport-routing.module';

@NgModule({
  imports: [SharedModule, TypeSportRoutingModule],
  declarations: [TypeSportComponent, TypeSportDetailComponent, TypeSportUpdateComponent, TypeSportDeleteDialogComponent],
  entryComponents: [TypeSportDeleteDialogComponent],
})
export class TypeSportModule {}
