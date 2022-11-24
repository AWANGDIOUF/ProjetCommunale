import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CibleComponent } from './list/cible.component';
import { CibleDetailComponent } from './detail/cible-detail.component';
import { CibleUpdateComponent } from './update/cible-update.component';
import { CibleDeleteDialogComponent } from './delete/cible-delete-dialog.component';
import { CibleRoutingModule } from './route/cible-routing.module';

@NgModule({
  imports: [SharedModule, CibleRoutingModule],
  declarations: [CibleComponent, CibleDetailComponent, CibleUpdateComponent, CibleDeleteDialogComponent],
  entryComponents: [CibleDeleteDialogComponent],
})
export class CibleModule {}
