import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecuperationRecyclableComponent } from './list/recuperation-recyclable.component';
import { RecuperationRecyclableDetailComponent } from './detail/recuperation-recyclable-detail.component';
import { RecuperationRecyclableUpdateComponent } from './update/recuperation-recyclable-update.component';
import { RecuperationRecyclableDeleteDialogComponent } from './delete/recuperation-recyclable-delete-dialog.component';
import { RecuperationRecyclableRoutingModule } from './route/recuperation-recyclable-routing.module';

@NgModule({
  imports: [SharedModule, RecuperationRecyclableRoutingModule],
  declarations: [
    RecuperationRecyclableComponent,
    RecuperationRecyclableDetailComponent,
    RecuperationRecyclableUpdateComponent,
    RecuperationRecyclableDeleteDialogComponent,
  ],
  entryComponents: [RecuperationRecyclableDeleteDialogComponent],
})
export class RecuperationRecyclableModule {}
