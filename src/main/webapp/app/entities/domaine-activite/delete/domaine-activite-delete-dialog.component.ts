import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomaineActivite } from '../domaine-activite.model';
import { DomaineActiviteService } from '../service/domaine-activite.service';

@Component({
  templateUrl: './domaine-activite-delete-dialog.component.html',
})
export class DomaineActiviteDeleteDialogComponent {
  domaineActivite?: IDomaineActivite;

  constructor(protected domaineActiviteService: DomaineActiviteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domaineActiviteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
