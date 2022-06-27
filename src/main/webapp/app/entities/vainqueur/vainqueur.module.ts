import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VainqueurComponent } from './list/vainqueur.component';
import { VainqueurDetailComponent } from './detail/vainqueur-detail.component';
import { VainqueurUpdateComponent } from './update/vainqueur-update.component';
import { VainqueurDeleteDialogComponent } from './delete/vainqueur-delete-dialog.component';
import { VainqueurRoutingModule } from './route/vainqueur-routing.module';

@NgModule({
  imports: [SharedModule, VainqueurRoutingModule],
  declarations: [VainqueurComponent, VainqueurDetailComponent, VainqueurUpdateComponent, VainqueurDeleteDialogComponent],
  entryComponents: [VainqueurDeleteDialogComponent],
})
export class VainqueurModule {}
