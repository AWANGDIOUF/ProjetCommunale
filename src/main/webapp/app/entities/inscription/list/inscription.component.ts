import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { InscriptionDeleteDialogComponent } from '../delete/inscription-delete-dialog.component';

@Component({
  selector: 'jhi-inscription',
  templateUrl: './inscription.component.html',
})
export class InscriptionComponent implements OnInit {
  inscriptions?: IInscription[];
  isLoading = false;

  constructor(protected inscriptionService: InscriptionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.inscriptionService.query().subscribe({
      next: (res: HttpResponse<IInscription[]>) => {
        this.isLoading = false;
        this.inscriptions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInscription): number {
    return item.id!;
  }

  delete(inscription: IInscription): void {
    const modalRef = this.modalService.open(InscriptionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inscription = inscription;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
