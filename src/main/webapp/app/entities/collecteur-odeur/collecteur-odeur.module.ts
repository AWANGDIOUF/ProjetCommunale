import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollecteurOdeurComponent } from './list/collecteur-odeur.component';
import { CollecteurOdeurDetailComponent } from './detail/collecteur-odeur-detail.component';
import { CollecteurOdeurUpdateComponent } from './update/collecteur-odeur-update.component';
import { CollecteurOdeurDeleteDialogComponent } from './delete/collecteur-odeur-delete-dialog.component';
import { CollecteurOdeurRoutingModule } from './route/collecteur-odeur-routing.module';

@NgModule({
  imports: [SharedModule, CollecteurOdeurRoutingModule],
  declarations: [
    CollecteurOdeurComponent,
    CollecteurOdeurDetailComponent,
    CollecteurOdeurUpdateComponent,
    CollecteurOdeurDeleteDialogComponent,
  ],
  entryComponents: [CollecteurOdeurDeleteDialogComponent],
})
export class CollecteurOdeurModule {}
