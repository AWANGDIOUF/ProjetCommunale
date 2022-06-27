import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeInterview } from '../demande-interview.model';
import { DemandeInterviewService } from '../service/demande-interview.service';
import { DemandeInterviewDeleteDialogComponent } from '../delete/demande-interview-delete-dialog.component';

@Component({
  selector: 'jhi-demande-interview',
  templateUrl: './demande-interview.component.html',
})
export class DemandeInterviewComponent implements OnInit {
  demandeInterviews?: IDemandeInterview[];
  isLoading = false;

  constructor(protected demandeInterviewService: DemandeInterviewService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.demandeInterviewService.query().subscribe({
      next: (res: HttpResponse<IDemandeInterview[]>) => {
        this.isLoading = false;
        this.demandeInterviews = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDemandeInterview): number {
    return item.id!;
  }

  delete(demandeInterview: IDemandeInterview): void {
    const modalRef = this.modalService.open(DemandeInterviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeInterview = demandeInterview;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
