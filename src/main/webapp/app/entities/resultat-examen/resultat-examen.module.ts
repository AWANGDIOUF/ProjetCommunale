import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResultatExamenComponent } from './list/resultat-examen.component';
import { ResultatExamenDetailComponent } from './detail/resultat-examen-detail.component';
import { ResultatExamenUpdateComponent } from './update/resultat-examen-update.component';
import { ResultatExamenDeleteDialogComponent } from './delete/resultat-examen-delete-dialog.component';
import { ResultatExamenRoutingModule } from './route/resultat-examen-routing.module';

@NgModule({
  imports: [SharedModule, ResultatExamenRoutingModule],
  declarations: [
    ResultatExamenComponent,
    ResultatExamenDetailComponent,
    ResultatExamenUpdateComponent,
    ResultatExamenDeleteDialogComponent,
  ],
  entryComponents: [ResultatExamenDeleteDialogComponent],
})
export class ResultatExamenModule {}
