import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecuperationRecyclable } from '../recuperation-recyclable.model';
import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';
import { RecuperationRecyclableDeleteDialogComponent } from '../delete/recuperation-recyclable-delete-dialog.component';

@Component({
  selector: 'jhi-recuperation-recyclable',
  templateUrl: './recuperation-recyclable.component.html',
})
export class RecuperationRecyclableComponent implements OnInit {
  recuperationRecyclables?: IRecuperationRecyclable[];
  isLoading = false;

  constructor(protected recuperationRecyclableService: RecuperationRecyclableService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.recuperationRecyclableService.query().subscribe({
      next: (res: HttpResponse<IRecuperationRecyclable[]>) => {
        this.isLoading = false;
        this.recuperationRecyclables = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRecuperationRecyclable): number {
    return item.id!;
  }

  delete(recuperationRecyclable: IRecuperationRecyclable): void {
    const modalRef = this.modalService.open(RecuperationRecyclableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recuperationRecyclable = recuperationRecyclable;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
