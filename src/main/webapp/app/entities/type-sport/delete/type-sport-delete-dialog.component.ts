import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeSport } from '../type-sport.model';
import { TypeSportService } from '../service/type-sport.service';

@Component({
  templateUrl: './type-sport-delete-dialog.component.html',
})
export class TypeSportDeleteDialogComponent {
  typeSport?: ITypeSport;

  constructor(protected typeSportService: TypeSportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeSportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
