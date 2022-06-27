import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntrepreneur } from '../entrepreneur.model';
import { EntrepreneurService } from '../service/entrepreneur.service';
import { EntrepreneurDeleteDialogComponent } from '../delete/entrepreneur-delete-dialog.component';

@Component({
  selector: 'jhi-entrepreneur',
  templateUrl: './entrepreneur.component.html',
})
export class EntrepreneurComponent implements OnInit {
  entrepreneurs?: IEntrepreneur[];
  isLoading = false;

  constructor(protected entrepreneurService: EntrepreneurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.entrepreneurService.query().subscribe({
      next: (res: HttpResponse<IEntrepreneur[]>) => {
        this.isLoading = false;
        this.entrepreneurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEntrepreneur): number {
    return item.id!;
  }

  delete(entrepreneur: IEntrepreneur): void {
    const modalRef = this.modalService.open(EntrepreneurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entrepreneur = entrepreneur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
