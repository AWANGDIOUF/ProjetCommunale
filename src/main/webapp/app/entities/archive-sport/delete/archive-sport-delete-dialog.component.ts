import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArchiveSport } from '../archive-sport.model';
import { ArchiveSportService } from '../service/archive-sport.service';

@Component({
  templateUrl: './archive-sport-delete-dialog.component.html',
})
export class ArchiveSportDeleteDialogComponent {
  archiveSport?: IArchiveSport;

  constructor(protected archiveSportService: ArchiveSportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.archiveSportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
