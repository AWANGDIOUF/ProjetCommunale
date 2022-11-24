import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';

@Component({
  templateUrl: './vainqueur-delete-dialog.component.html',
})
export class VainqueurDeleteDialogComponent {
  vainqueur?: IVainqueur;

  constructor(protected vainqueurService: VainqueurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vainqueurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
