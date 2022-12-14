import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';

@Component({
  templateUrl: './competition-delete-dialog.component.html',
})
export class CompetitionDeleteDialogComponent {
  competition?: ICompetition;

  constructor(protected competitionService: CompetitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.competitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
