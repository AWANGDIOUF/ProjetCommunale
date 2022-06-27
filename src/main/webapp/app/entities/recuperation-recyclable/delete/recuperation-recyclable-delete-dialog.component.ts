import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecuperationRecyclable } from '../recuperation-recyclable.model';
import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';

@Component({
  templateUrl: './recuperation-recyclable-delete-dialog.component.html',
})
export class RecuperationRecyclableDeleteDialogComponent {
  recuperationRecyclable?: IRecuperationRecyclable;

  constructor(protected recuperationRecyclableService: RecuperationRecyclableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recuperationRecyclableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
