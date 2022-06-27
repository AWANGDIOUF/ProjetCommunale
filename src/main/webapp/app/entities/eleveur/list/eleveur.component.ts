import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEleveur } from '../eleveur.model';
import { EleveurService } from '../service/eleveur.service';
import { EleveurDeleteDialogComponent } from '../delete/eleveur-delete-dialog.component';

@Component({
  selector: 'jhi-eleveur',
  templateUrl: './eleveur.component.html',
})
export class EleveurComponent implements OnInit {
  eleveurs?: IEleveur[];
  isLoading = false;

  constructor(protected eleveurService: EleveurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eleveurService.query().subscribe({
      next: (res: HttpResponse<IEleveur[]>) => {
        this.isLoading = false;
        this.eleveurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEleveur): number {
    return item.id!;
  }

  delete(eleveur: IEleveur): void {
    const modalRef = this.modalService.open(EleveurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eleveur = eleveur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
