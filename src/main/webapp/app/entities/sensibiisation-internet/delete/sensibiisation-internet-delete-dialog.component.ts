import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensibiisationInternet } from '../sensibiisation-internet.model';
import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';

@Component({
  templateUrl: './sensibiisation-internet-delete-dialog.component.html',
})
export class SensibiisationInternetDeleteDialogComponent {
  sensibiisationInternet?: ISensibiisationInternet;

  constructor(protected sensibiisationInternetService: SensibiisationInternetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensibiisationInternetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
