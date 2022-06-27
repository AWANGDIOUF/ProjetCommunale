import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntreprenariat } from '../entreprenariat.model';
import { EntreprenariatService } from '../service/entreprenariat.service';
import { EntreprenariatDeleteDialogComponent } from '../delete/entreprenariat-delete-dialog.component';

@Component({
  selector: 'jhi-entreprenariat',
  templateUrl: './entreprenariat.component.html',
})
export class EntreprenariatComponent implements OnInit {
  entreprenariats?: IEntreprenariat[];
  isLoading = false;

  constructor(protected entreprenariatService: EntreprenariatService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.entreprenariatService.query().subscribe({
      next: (res: HttpResponse<IEntreprenariat[]>) => {
        this.isLoading = false;
        this.entreprenariats = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEntreprenariat): number {
    return item.id!;
  }

  delete(entreprenariat: IEntreprenariat): void {
    const modalRef = this.modalService.open(EntreprenariatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entreprenariat = entreprenariat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
