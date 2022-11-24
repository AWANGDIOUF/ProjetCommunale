import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatch } from '../match.model';
import { MatchService } from '../service/match.service';

@Component({
  templateUrl: './match-delete-dialog.component.html',
})
export class MatchDeleteDialogComponent {
  match?: IMatch;

  constructor(protected matchService: MatchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
