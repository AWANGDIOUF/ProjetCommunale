import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultatExamen } from '../resultat-examen.model';
import { ResultatExamenService } from '../service/resultat-examen.service';

@Component({
  templateUrl: './resultat-examen-delete-dialog.component.html',
})
export class ResultatExamenDeleteDialogComponent {
  resultatExamen?: IResultatExamen;

  constructor(protected resultatExamenService: ResultatExamenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resultatExamenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
