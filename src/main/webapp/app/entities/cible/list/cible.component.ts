import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICible } from '../cible.model';
import { CibleService } from '../service/cible.service';
import { CibleDeleteDialogComponent } from '../delete/cible-delete-dialog.component';

@Component({
  selector: 'jhi-cible',
  templateUrl: './cible.component.html',
})
export class CibleComponent implements OnInit {
  cibles?: ICible[];
  isLoading = false;

  constructor(protected cibleService: CibleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cibleService.query().subscribe({
      next: (res: HttpResponse<ICible[]>) => {
        this.isLoading = false;
        this.cibles = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICible): number {
    return item.id!;
  }

  delete(cible: ICible): void {
    const modalRef = this.modalService.open(CibleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cible = cible;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
