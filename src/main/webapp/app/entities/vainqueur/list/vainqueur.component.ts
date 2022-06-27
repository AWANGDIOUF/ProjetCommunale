import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';
import { VainqueurDeleteDialogComponent } from '../delete/vainqueur-delete-dialog.component';

@Component({
  selector: 'jhi-vainqueur',
  templateUrl: './vainqueur.component.html',
})
export class VainqueurComponent implements OnInit {
  vainqueurs?: IVainqueur[];
  isLoading = false;

  constructor(protected vainqueurService: VainqueurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vainqueurService.query().subscribe({
      next: (res: HttpResponse<IVainqueur[]>) => {
        this.isLoading = false;
        this.vainqueurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IVainqueur): number {
    return item.id!;
  }

  delete(vainqueur: IVainqueur): void {
    const modalRef = this.modalService.open(VainqueurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vainqueur = vainqueur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
