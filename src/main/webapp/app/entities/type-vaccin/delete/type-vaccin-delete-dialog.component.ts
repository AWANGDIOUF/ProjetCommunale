import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';

@Component({
  templateUrl: './type-vaccin-delete-dialog.component.html',
})
export class TypeVaccinDeleteDialogComponent {
  typeVaccin?: ITypeVaccin;

  constructor(protected typeVaccinService: TypeVaccinService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeVaccinService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
