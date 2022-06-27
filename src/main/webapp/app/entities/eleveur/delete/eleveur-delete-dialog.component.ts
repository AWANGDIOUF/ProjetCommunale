import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEleveur } from '../eleveur.model';
import { EleveurService } from '../service/eleveur.service';

@Component({
  templateUrl: './eleveur-delete-dialog.component.html',
})
export class EleveurDeleteDialogComponent {
  eleveur?: IEleveur;

  constructor(protected eleveurService: EleveurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eleveurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
