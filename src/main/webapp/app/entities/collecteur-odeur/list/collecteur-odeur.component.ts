import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollecteurOdeur } from '../collecteur-odeur.model';
import { CollecteurOdeurService } from '../service/collecteur-odeur.service';
import { CollecteurOdeurDeleteDialogComponent } from '../delete/collecteur-odeur-delete-dialog.component';

@Component({
  selector: 'jhi-collecteur-odeur',
  templateUrl: './collecteur-odeur.component.html',
})
export class CollecteurOdeurComponent implements OnInit {
  collecteurOdeurs?: ICollecteurOdeur[];
  isLoading = false;

  constructor(protected collecteurOdeurService: CollecteurOdeurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.collecteurOdeurService.query().subscribe({
      next: (res: HttpResponse<ICollecteurOdeur[]>) => {
        this.isLoading = false;
        this.collecteurOdeurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICollecteurOdeur): number {
    return item.id!;
  }

  delete(collecteurOdeur: ICollecteurOdeur): void {
    const modalRef = this.modalService.open(CollecteurOdeurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.collecteurOdeur = collecteurOdeur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
