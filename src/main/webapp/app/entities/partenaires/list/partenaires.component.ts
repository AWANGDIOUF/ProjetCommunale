import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartenaires } from '../partenaires.model';
import { PartenairesService } from '../service/partenaires.service';
import { PartenairesDeleteDialogComponent } from '../delete/partenaires-delete-dialog.component';

@Component({
  selector: 'jhi-partenaires',
  templateUrl: './partenaires.component.html',
})
export class PartenairesComponent implements OnInit {
  partenaires?: IPartenaires[];
  isLoading = false;

  constructor(protected partenairesService: PartenairesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.partenairesService.query().subscribe({
      next: (res: HttpResponse<IPartenaires[]>) => {
        this.isLoading = false;
        this.partenaires = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPartenaires): number {
    return item.id!;
  }

  delete(partenaires: IPartenaires): void {
    const modalRef = this.modalService.open(PartenairesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partenaires = partenaires;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
