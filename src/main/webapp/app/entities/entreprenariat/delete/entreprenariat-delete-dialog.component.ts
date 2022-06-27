import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntreprenariat } from '../entreprenariat.model';
import { EntreprenariatService } from '../service/entreprenariat.service';

@Component({
  templateUrl: './entreprenariat-delete-dialog.component.html',
})
export class EntreprenariatDeleteDialogComponent {
  entreprenariat?: IEntreprenariat;

  constructor(protected entreprenariatService: EntreprenariatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entreprenariatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
