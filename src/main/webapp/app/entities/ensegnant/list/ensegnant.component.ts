import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnsegnant } from '../ensegnant.model';
import { EnsegnantService } from '../service/ensegnant.service';
import { EnsegnantDeleteDialogComponent } from '../delete/ensegnant-delete-dialog.component';

@Component({
  selector: 'jhi-ensegnant',
  templateUrl: './ensegnant.component.html',
})
export class EnsegnantComponent implements OnInit {
  ensegnants?: IEnsegnant[];
  isLoading = false;

  constructor(protected ensegnantService: EnsegnantService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ensegnantService.query().subscribe({
      next: (res: HttpResponse<IEnsegnant[]>) => {
        this.isLoading = false;
        this.ensegnants = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEnsegnant): number {
    return item.id!;
  }

  delete(ensegnant: IEnsegnant): void {
    const modalRef = this.modalService.open(EnsegnantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ensegnant = ensegnant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
