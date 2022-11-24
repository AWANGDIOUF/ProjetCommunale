import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICible } from '../cible.model';
import { CibleService } from '../service/cible.service';

@Component({
  templateUrl: './cible-delete-dialog.component.html',
})
export class CibleDeleteDialogComponent {
  cible?: ICible;

  constructor(protected cibleService: CibleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cibleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
