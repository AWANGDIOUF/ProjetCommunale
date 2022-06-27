import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonneur } from '../donneur.model';
import { DonneurService } from '../service/donneur.service';

@Component({
  templateUrl: './donneur-delete-dialog.component.html',
})
export class DonneurDeleteDialogComponent {
  donneur?: IDonneur;

  constructor(protected donneurService: DonneurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donneurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
