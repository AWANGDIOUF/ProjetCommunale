import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDon } from '../don.model';
import { DonService } from '../service/don.service';
import { DonDeleteDialogComponent } from '../delete/don-delete-dialog.component';

@Component({
  selector: 'jhi-don',
  templateUrl: './don.component.html',
})
export class DonComponent implements OnInit {
  dons?: IDon[];
  isLoading = false;

  constructor(protected donService: DonService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.donService.query().subscribe({
      next: (res: HttpResponse<IDon[]>) => {
        this.isLoading = false;
        this.dons = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDon): number {
    return item.id!;
  }

  delete(don: IDon): void {
    const modalRef = this.modalService.open(DonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.don = don;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
