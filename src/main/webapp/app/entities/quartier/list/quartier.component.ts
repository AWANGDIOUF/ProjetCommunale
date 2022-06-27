import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';
import { QuartierDeleteDialogComponent } from '../delete/quartier-delete-dialog.component';

@Component({
  selector: 'jhi-quartier',
  templateUrl: './quartier.component.html',
})
export class QuartierComponent implements OnInit {
  quartiers?: IQuartier[];
  isLoading = false;

  constructor(protected quartierService: QuartierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.quartierService.query().subscribe({
      next: (res: HttpResponse<IQuartier[]>) => {
        this.isLoading = false;
        this.quartiers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IQuartier): number {
    return item.id!;
  }

  delete(quartier: IQuartier): void {
    const modalRef = this.modalService.open(QuartierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.quartier = quartier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
