import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CombattantComponent } from './list/combattant.component';
import { CombattantDetailComponent } from './detail/combattant-detail.component';
import { CombattantUpdateComponent } from './update/combattant-update.component';
import { CombattantDeleteDialogComponent } from './delete/combattant-delete-dialog.component';
import { CombattantRoutingModule } from './route/combattant-routing.module';

@NgModule({
  imports: [SharedModule, CombattantRoutingModule],
  declarations: [CombattantComponent, CombattantDetailComponent, CombattantUpdateComponent, CombattantDeleteDialogComponent],
  entryComponents: [CombattantDeleteDialogComponent],
})
export class CombattantModule {}
