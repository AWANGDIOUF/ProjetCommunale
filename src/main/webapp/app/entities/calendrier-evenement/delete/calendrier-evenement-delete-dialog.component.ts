import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICalendrierEvenement } from '../calendrier-evenement.model';
import { CalendrierEvenementService } from '../service/calendrier-evenement.service';

@Component({
  templateUrl: './calendrier-evenement-delete-dialog.component.html',
})
export class CalendrierEvenementDeleteDialogComponent {
  calendrierEvenement?: ICalendrierEvenement;

  constructor(protected calendrierEvenementService: CalendrierEvenementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calendrierEvenementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
