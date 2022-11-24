import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonneurComponent } from './list/donneur.component';
import { DonneurDetailComponent } from './detail/donneur-detail.component';
import { DonneurUpdateComponent } from './update/donneur-update.component';
import { DonneurDeleteDialogComponent } from './delete/donneur-delete-dialog.component';
import { DonneurRoutingModule } from './route/donneur-routing.module';

@NgModule({
  imports: [SharedModule, DonneurRoutingModule],
  declarations: [DonneurComponent, DonneurDetailComponent, DonneurUpdateComponent, DonneurDeleteDialogComponent],
  entryComponents: [DonneurDeleteDialogComponent],
})
export class DonneurModule {}
