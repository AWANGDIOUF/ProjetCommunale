import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILienTutoriel } from '../lien-tutoriel.model';
import { LienTutorielService } from '../service/lien-tutoriel.service';
import { LienTutorielDeleteDialogComponent } from '../delete/lien-tutoriel-delete-dialog.component';

@Component({
  selector: 'jhi-lien-tutoriel',
  templateUrl: './lien-tutoriel.component.html',
})
export class LienTutorielComponent implements OnInit {
  lienTutoriels?: ILienTutoriel[];
  isLoading = false;

  constructor(protected lienTutorielService: LienTutorielService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lienTutorielService.query().subscribe({
      next: (res: HttpResponse<ILienTutoriel[]>) => {
        this.isLoading = false;
        this.lienTutoriels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILienTutoriel): number {
    return item.id!;
  }

  delete(lienTutoriel: ILienTutoriel): void {
    const modalRef = this.modalService.open(LienTutorielDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lienTutoriel = lienTutoriel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
