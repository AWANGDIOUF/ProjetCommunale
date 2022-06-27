import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BeneficiaireComponent } from './list/beneficiaire.component';
import { BeneficiaireDetailComponent } from './detail/beneficiaire-detail.component';
import { BeneficiaireUpdateComponent } from './update/beneficiaire-update.component';
import { BeneficiaireDeleteDialogComponent } from './delete/beneficiaire-delete-dialog.component';
import { BeneficiaireRoutingModule } from './route/beneficiaire-routing.module';

@NgModule({
  imports: [SharedModule, BeneficiaireRoutingModule],
  declarations: [BeneficiaireComponent, BeneficiaireDetailComponent, BeneficiaireUpdateComponent, BeneficiaireDeleteDialogComponent],
  entryComponents: [BeneficiaireDeleteDialogComponent],
})
export class BeneficiaireModule {}
