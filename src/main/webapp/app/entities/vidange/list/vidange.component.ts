import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVidange } from '../vidange.model';
import { VidangeService } from '../service/vidange.service';
import { VidangeDeleteDialogComponent } from '../delete/vidange-delete-dialog.component';

@Component({
  selector: 'jhi-vidange',
  templateUrl: './vidange.component.html',
})
export class VidangeComponent implements OnInit {
  vidanges?: IVidange[];
  isLoading = false;

  constructor(protected vidangeService: VidangeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vidangeService.query().subscribe({
      next: (res: HttpResponse<IVidange[]>) => {
        this.isLoading = false;
        this.vidanges = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IVidange): number {
    return item.id!;
  }

  delete(vidange: IVidange): void {
    const modalRef = this.modalService.open(VidangeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vidange = vidange;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
