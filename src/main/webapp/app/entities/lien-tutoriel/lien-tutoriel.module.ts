import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LienTutorielComponent } from './list/lien-tutoriel.component';
import { LienTutorielDetailComponent } from './detail/lien-tutoriel-detail.component';
import { LienTutorielUpdateComponent } from './update/lien-tutoriel-update.component';
import { LienTutorielDeleteDialogComponent } from './delete/lien-tutoriel-delete-dialog.component';
import { LienTutorielRoutingModule } from './route/lien-tutoriel-routing.module';

@NgModule({
  imports: [SharedModule, LienTutorielRoutingModule],
  declarations: [LienTutorielComponent, LienTutorielDetailComponent, LienTutorielUpdateComponent, LienTutorielDeleteDialogComponent],
  entryComponents: [LienTutorielDeleteDialogComponent],
})
export class LienTutorielModule {}
