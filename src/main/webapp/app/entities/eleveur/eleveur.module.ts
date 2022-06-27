import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EleveurComponent } from './list/eleveur.component';
import { EleveurDetailComponent } from './detail/eleveur-detail.component';
import { EleveurUpdateComponent } from './update/eleveur-update.component';
import { EleveurDeleteDialogComponent } from './delete/eleveur-delete-dialog.component';
import { EleveurRoutingModule } from './route/eleveur-routing.module';

@NgModule({
  imports: [SharedModule, EleveurRoutingModule],
  declarations: [EleveurComponent, EleveurDetailComponent, EleveurUpdateComponent, EleveurDeleteDialogComponent],
  entryComponents: [EleveurDeleteDialogComponent],
})
export class EleveurModule {}
