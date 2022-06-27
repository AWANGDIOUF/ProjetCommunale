import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EntrepreneurComponent } from './list/entrepreneur.component';
import { EntrepreneurDetailComponent } from './detail/entrepreneur-detail.component';
import { EntrepreneurUpdateComponent } from './update/entrepreneur-update.component';
import { EntrepreneurDeleteDialogComponent } from './delete/entrepreneur-delete-dialog.component';
import { EntrepreneurRoutingModule } from './route/entrepreneur-routing.module';

@NgModule({
  imports: [SharedModule, EntrepreneurRoutingModule],
  declarations: [EntrepreneurComponent, EntrepreneurDetailComponent, EntrepreneurUpdateComponent, EntrepreneurDeleteDialogComponent],
  entryComponents: [EntrepreneurDeleteDialogComponent],
})
export class EntrepreneurModule {}
