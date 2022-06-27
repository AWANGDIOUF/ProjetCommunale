import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeneficiaire } from '../beneficiaire.model';
import { BeneficiaireService } from '../service/beneficiaire.service';

@Component({
  templateUrl: './beneficiaire-delete-dialog.component.html',
})
export class BeneficiaireDeleteDialogComponent {
  beneficiaire?: IBeneficiaire;

  constructor(protected beneficiaireService: BeneficiaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.beneficiaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
