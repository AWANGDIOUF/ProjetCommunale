import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewsArtiste } from '../interviews-artiste.model';
import { InterviewsArtisteService } from '../service/interviews-artiste.service';

@Component({
  templateUrl: './interviews-artiste-delete-dialog.component.html',
})
export class InterviewsArtisteDeleteDialogComponent {
  interviewsArtiste?: IInterviewsArtiste;

  constructor(protected interviewsArtisteService: InterviewsArtisteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewsArtisteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
