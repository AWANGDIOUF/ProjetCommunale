import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollecteurOdeur } from '../collecteur-odeur.model';
import { CollecteurOdeurService } from '../service/collecteur-odeur.service';

@Component({
  templateUrl: './collecteur-odeur-delete-dialog.component.html',
})
export class CollecteurOdeurDeleteDialogComponent {
  collecteurOdeur?: ICollecteurOdeur;

  constructor(protected collecteurOdeurService: CollecteurOdeurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.collecteurOdeurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
