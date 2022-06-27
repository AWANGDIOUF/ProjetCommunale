import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArtiste } from '../artiste.model';
import { ArtisteService } from '../service/artiste.service';

@Component({
  templateUrl: './artiste-delete-dialog.component.html',
})
export class ArtisteDeleteDialogComponent {
  artiste?: IArtiste;

  constructor(protected artisteService: ArtisteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.artisteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
