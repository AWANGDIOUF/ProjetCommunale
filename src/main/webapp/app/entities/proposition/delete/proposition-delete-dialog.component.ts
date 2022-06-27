import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposition } from '../proposition.model';
import { PropositionService } from '../service/proposition.service';

@Component({
  templateUrl: './proposition-delete-dialog.component.html',
})
export class PropositionDeleteDialogComponent {
  proposition?: IProposition;

  constructor(protected propositionService: PropositionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propositionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
