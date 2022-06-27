import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnsegnant } from '../ensegnant.model';
import { EnsegnantService } from '../service/ensegnant.service';

@Component({
  templateUrl: './ensegnant-delete-dialog.component.html',
})
export class EnsegnantDeleteDialogComponent {
  ensegnant?: IEnsegnant;

  constructor(protected ensegnantService: EnsegnantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ensegnantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
