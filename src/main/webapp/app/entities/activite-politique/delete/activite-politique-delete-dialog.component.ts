import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivitePolitique } from '../activite-politique.model';
import { ActivitePolitiqueService } from '../service/activite-politique.service';

@Component({
  templateUrl: './activite-politique-delete-dialog.component.html',
})
export class ActivitePolitiqueDeleteDialogComponent {
  activitePolitique?: IActivitePolitique;

  constructor(protected activitePolitiqueService: ActivitePolitiqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activitePolitiqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
