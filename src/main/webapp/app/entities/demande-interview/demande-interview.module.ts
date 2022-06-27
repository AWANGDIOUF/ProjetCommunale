import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeInterviewComponent } from './list/demande-interview.component';
import { DemandeInterviewDetailComponent } from './detail/demande-interview-detail.component';
import { DemandeInterviewUpdateComponent } from './update/demande-interview-update.component';
import { DemandeInterviewDeleteDialogComponent } from './delete/demande-interview-delete-dialog.component';
import { DemandeInterviewRoutingModule } from './route/demande-interview-routing.module';

@NgModule({
  imports: [SharedModule, DemandeInterviewRoutingModule],
  declarations: [
    DemandeInterviewComponent,
    DemandeInterviewDetailComponent,
    DemandeInterviewUpdateComponent,
    DemandeInterviewDeleteDialogComponent,
  ],
  entryComponents: [DemandeInterviewDeleteDialogComponent],
})
export class DemandeInterviewModule {}
