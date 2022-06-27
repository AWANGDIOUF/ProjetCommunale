import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CalendrierEvenementComponent } from './list/calendrier-evenement.component';
import { CalendrierEvenementDetailComponent } from './detail/calendrier-evenement-detail.component';
import { CalendrierEvenementUpdateComponent } from './update/calendrier-evenement-update.component';
import { CalendrierEvenementDeleteDialogComponent } from './delete/calendrier-evenement-delete-dialog.component';
import { CalendrierEvenementRoutingModule } from './route/calendrier-evenement-routing.module';

@NgModule({
  imports: [SharedModule, CalendrierEvenementRoutingModule],
  declarations: [
    CalendrierEvenementComponent,
    CalendrierEvenementDetailComponent,
    CalendrierEvenementUpdateComponent,
    CalendrierEvenementDeleteDialogComponent,
  ],
  entryComponents: [CalendrierEvenementDeleteDialogComponent],
})
export class CalendrierEvenementModule {}
