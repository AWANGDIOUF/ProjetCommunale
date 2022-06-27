import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILienTutoriel } from '../lien-tutoriel.model';
import { LienTutorielService } from '../service/lien-tutoriel.service';

@Component({
  templateUrl: './lien-tutoriel-delete-dialog.component.html',
})
export class LienTutorielDeleteDialogComponent {
  lienTutoriel?: ILienTutoriel;

  constructor(protected lienTutorielService: LienTutorielService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lienTutorielService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
