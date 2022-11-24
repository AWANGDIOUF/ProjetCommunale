import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICombattant } from '../combattant.model';
import { CombattantService } from '../service/combattant.service';

@Component({
  templateUrl: './combattant-delete-dialog.component.html',
})
export class CombattantDeleteDialogComponent {
  combattant?: ICombattant;

  constructor(protected combattantService: CombattantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.combattantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
