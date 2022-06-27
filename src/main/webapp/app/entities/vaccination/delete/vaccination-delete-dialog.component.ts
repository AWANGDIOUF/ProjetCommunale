import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVaccination } from '../vaccination.model';
import { VaccinationService } from '../service/vaccination.service';

@Component({
  templateUrl: './vaccination-delete-dialog.component.html',
})
export class VaccinationDeleteDialogComponent {
  vaccination?: IVaccination;

  constructor(protected vaccinationService: VaccinationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vaccinationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
