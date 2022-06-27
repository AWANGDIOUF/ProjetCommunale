import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArchiveSportComponent } from './list/archive-sport.component';
import { ArchiveSportDetailComponent } from './detail/archive-sport-detail.component';
import { ArchiveSportUpdateComponent } from './update/archive-sport-update.component';
import { ArchiveSportDeleteDialogComponent } from './delete/archive-sport-delete-dialog.component';
import { ArchiveSportRoutingModule } from './route/archive-sport-routing.module';

@NgModule({
  imports: [SharedModule, ArchiveSportRoutingModule],
  declarations: [ArchiveSportComponent, ArchiveSportDetailComponent, ArchiveSportUpdateComponent, ArchiveSportDeleteDialogComponent],
  entryComponents: [ArchiveSportDeleteDialogComponent],
})
export class ArchiveSportModule {}
