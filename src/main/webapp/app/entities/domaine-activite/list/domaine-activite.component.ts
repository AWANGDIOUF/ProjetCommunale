import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomaineActivite } from '../domaine-activite.model';
import { DomaineActiviteService } from '../service/domaine-activite.service';
import { DomaineActiviteDeleteDialogComponent } from '../delete/domaine-activite-delete-dialog.component';

@Component({
  selector: 'jhi-domaine-activite',
  templateUrl: './domaine-activite.component.html',
})
export class DomaineActiviteComponent implements OnInit {
  domaineActivites?: IDomaineActivite[];
  isLoading = false;

  constructor(protected domaineActiviteService: DomaineActiviteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.domaineActiviteService.query().subscribe({
      next: (res: HttpResponse<IDomaineActivite[]>) => {
        this.isLoading = false;
        this.domaineActivites = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDomaineActivite): number {
    return item.id!;
  }

  delete(domaineActivite: IDomaineActivite): void {
    const modalRef = this.modalService.open(DomaineActiviteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.domaineActivite = domaineActivite;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
