import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonSang } from '../don-sang.model';
import { DonSangService } from '../service/don-sang.service';

@Component({
  templateUrl: './don-sang-delete-dialog.component.html',
})
export class DonSangDeleteDialogComponent {
  donSang?: IDonSang;

  constructor(protected donSangService: DonSangService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donSangService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
