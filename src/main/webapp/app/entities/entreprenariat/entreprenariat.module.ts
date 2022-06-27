import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EntreprenariatComponent } from './list/entreprenariat.component';
import { EntreprenariatDetailComponent } from './detail/entreprenariat-detail.component';
import { EntreprenariatUpdateComponent } from './update/entreprenariat-update.component';
import { EntreprenariatDeleteDialogComponent } from './delete/entreprenariat-delete-dialog.component';
import { EntreprenariatRoutingModule } from './route/entreprenariat-routing.module';

@NgModule({
  imports: [SharedModule, EntreprenariatRoutingModule],
  declarations: [
    EntreprenariatComponent,
    EntreprenariatDetailComponent,
    EntreprenariatUpdateComponent,
    EntreprenariatDeleteDialogComponent,
  ],
  entryComponents: [EntreprenariatDeleteDialogComponent],
})
export class EntreprenariatModule {}
