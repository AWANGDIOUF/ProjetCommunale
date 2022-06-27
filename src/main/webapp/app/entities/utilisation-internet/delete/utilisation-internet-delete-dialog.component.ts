import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilisationInternet } from '../utilisation-internet.model';
import { UtilisationInternetService } from '../service/utilisation-internet.service';

@Component({
  templateUrl: './utilisation-internet-delete-dialog.component.html',
})
export class UtilisationInternetDeleteDialogComponent {
  utilisationInternet?: IUtilisationInternet;

  constructor(protected utilisationInternetService: UtilisationInternetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilisationInternetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
