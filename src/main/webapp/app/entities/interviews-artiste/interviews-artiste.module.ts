import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InterviewsArtisteComponent } from './list/interviews-artiste.component';
import { InterviewsArtisteDetailComponent } from './detail/interviews-artiste-detail.component';
import { InterviewsArtisteUpdateComponent } from './update/interviews-artiste-update.component';
import { InterviewsArtisteDeleteDialogComponent } from './delete/interviews-artiste-delete-dialog.component';
import { InterviewsArtisteRoutingModule } from './route/interviews-artiste-routing.module';

@NgModule({
  imports: [SharedModule, InterviewsArtisteRoutingModule],
  declarations: [
    InterviewsArtisteComponent,
    InterviewsArtisteDetailComponent,
    InterviewsArtisteUpdateComponent,
    InterviewsArtisteDeleteDialogComponent,
  ],
  entryComponents: [InterviewsArtisteDeleteDialogComponent],
})
export class InterviewsArtisteModule {}
