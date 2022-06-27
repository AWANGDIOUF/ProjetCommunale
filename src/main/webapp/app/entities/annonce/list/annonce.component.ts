import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnonce } from '../annonce.model';
import { AnnonceService } from '../service/annonce.service';
import { AnnonceDeleteDialogComponent } from '../delete/annonce-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-annonce',
  templateUrl: './annonce.component.html',
})
export class AnnonceComponent implements OnInit {
  annonces?: IAnnonce[];
  isLoading = false;

  constructor(protected annonceService: AnnonceService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.annonceService.query().subscribe({
      next: (res: HttpResponse<IAnnonce[]>) => {
        this.isLoading = false;
        this.annonces = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAnnonce): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(annonce: IAnnonce): void {
    const modalRef = this.modalService.open(AnnonceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.annonce = annonce;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
