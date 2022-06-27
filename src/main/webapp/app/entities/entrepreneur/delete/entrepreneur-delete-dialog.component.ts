import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntrepreneur } from '../entrepreneur.model';
import { EntrepreneurService } from '../service/entrepreneur.service';

@Component({
  templateUrl: './entrepreneur-delete-dialog.component.html',
})
export class EntrepreneurDeleteDialogComponent {
  entrepreneur?: IEntrepreneur;

  constructor(protected entrepreneurService: EntrepreneurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entrepreneurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
