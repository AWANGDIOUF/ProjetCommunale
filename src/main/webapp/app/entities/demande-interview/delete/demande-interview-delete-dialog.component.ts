import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeInterview } from '../demande-interview.model';
import { DemandeInterviewService } from '../service/demande-interview.service';

@Component({
  templateUrl: './demande-interview-delete-dialog.component.html',
})
export class DemandeInterviewDeleteDialogComponent {
  demandeInterview?: IDemandeInterview;

  constructor(protected demandeInterviewService: DemandeInterviewService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeInterviewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
