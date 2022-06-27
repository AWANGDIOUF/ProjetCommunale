import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActivitePolitiqueComponent } from './list/activite-politique.component';
import { ActivitePolitiqueDetailComponent } from './detail/activite-politique-detail.component';
import { ActivitePolitiqueUpdateComponent } from './update/activite-politique-update.component';
import { ActivitePolitiqueDeleteDialogComponent } from './delete/activite-politique-delete-dialog.component';
import { ActivitePolitiqueRoutingModule } from './route/activite-politique-routing.module';

@NgModule({
  imports: [SharedModule, ActivitePolitiqueRoutingModule],
  declarations: [
    ActivitePolitiqueComponent,
    ActivitePolitiqueDetailComponent,
    ActivitePolitiqueUpdateComponent,
    ActivitePolitiqueDeleteDialogComponent,
  ],
  entryComponents: [ActivitePolitiqueDeleteDialogComponent],
})
export class ActivitePolitiqueModule {}
