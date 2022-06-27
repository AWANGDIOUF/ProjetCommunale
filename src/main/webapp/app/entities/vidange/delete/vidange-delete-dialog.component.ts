import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVidange } from '../vidange.model';
import { VidangeService } from '../service/vidange.service';

@Component({
  templateUrl: './vidange-delete-dialog.component.html',
})
export class VidangeDeleteDialogComponent {
  vidange?: IVidange;

  constructor(protected vidangeService: VidangeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vidangeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
