import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultatExamen } from '../resultat-examen.model';
import { ResultatExamenService } from '../service/resultat-examen.service';
import { ResultatExamenDeleteDialogComponent } from '../delete/resultat-examen-delete-dialog.component';

@Component({
  selector: 'jhi-resultat-examen',
  templateUrl: './resultat-examen.component.html',
})
export class ResultatExamenComponent implements OnInit {
  resultatExamen?: IResultatExamen[];
  isLoading = false;

  constructor(protected resultatExamenService: ResultatExamenService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.resultatExamenService.query().subscribe({
      next: (res: HttpResponse<IResultatExamen[]>) => {
        this.isLoading = false;
        this.resultatExamen = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IResultatExamen): number {
    return item.id!;
  }

  delete(resultatExamen: IResultatExamen): void {
    const modalRef = this.modalService.open(ResultatExamenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resultatExamen = resultatExamen;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
