import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArtiste } from '../artiste.model';
import { ArtisteService } from '../service/artiste.service';
import { ArtisteDeleteDialogComponent } from '../delete/artiste-delete-dialog.component';

@Component({
  selector: 'jhi-artiste',
  templateUrl: './artiste.component.html',
})
export class ArtisteComponent implements OnInit {
  artistes?: IArtiste[];
  isLoading = false;

  constructor(protected artisteService: ArtisteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.artisteService.query().subscribe({
      next: (res: HttpResponse<IArtiste[]>) => {
        this.isLoading = false;
        this.artistes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IArtiste): number {
    return item.id!;
  }

  delete(artiste: IArtiste): void {
    const modalRef = this.modalService.open(ArtisteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.artiste = artiste;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
