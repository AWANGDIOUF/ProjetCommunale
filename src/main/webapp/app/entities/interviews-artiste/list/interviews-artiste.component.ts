import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewsArtiste } from '../interviews-artiste.model';
import { InterviewsArtisteService } from '../service/interviews-artiste.service';
import { InterviewsArtisteDeleteDialogComponent } from '../delete/interviews-artiste-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-interviews-artiste',
  templateUrl: './interviews-artiste.component.html',
})
export class InterviewsArtisteComponent implements OnInit {
  interviewsArtistes?: IInterviewsArtiste[];
  isLoading = false;

  constructor(
    protected interviewsArtisteService: InterviewsArtisteService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.interviewsArtisteService.query().subscribe({
      next: (res: HttpResponse<IInterviewsArtiste[]>) => {
        this.isLoading = false;
        this.interviewsArtistes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInterviewsArtiste): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(interviewsArtiste: IInterviewsArtiste): void {
    const modalRef = this.modalService.open(InterviewsArtisteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interviewsArtiste = interviewsArtiste;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
