import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArtisteComponent } from './list/artiste.component';
import { ArtisteDetailComponent } from './detail/artiste-detail.component';
import { ArtisteUpdateComponent } from './update/artiste-update.component';
import { ArtisteDeleteDialogComponent } from './delete/artiste-delete-dialog.component';
import { ArtisteRoutingModule } from './route/artiste-routing.module';

@NgModule({
  imports: [SharedModule, ArtisteRoutingModule],
  declarations: [ArtisteComponent, ArtisteDetailComponent, ArtisteUpdateComponent, ArtisteDeleteDialogComponent],
  entryComponents: [ArtisteDeleteDialogComponent],
})
export class ArtisteModule {}
