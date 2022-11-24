import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDon } from '../don.model';
import { DonService } from '../service/don.service';

@Component({
  templateUrl: './don-delete-dialog.component.html',
})
export class DonDeleteDialogComponent {
  don?: IDon;

  constructor(protected donService: DonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
