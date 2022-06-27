import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArchiveSport } from '../archive-sport.model';
import { ArchiveSportService } from '../service/archive-sport.service';
import { ArchiveSportDeleteDialogComponent } from '../delete/archive-sport-delete-dialog.component';

@Component({
  selector: 'jhi-archive-sport',
  templateUrl: './archive-sport.component.html',
})
export class ArchiveSportComponent implements OnInit {
  archiveSports?: IArchiveSport[];
  isLoading = false;

  constructor(protected archiveSportService: ArchiveSportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.archiveSportService.query().subscribe({
      next: (res: HttpResponse<IArchiveSport[]>) => {
        this.isLoading = false;
        this.archiveSports = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IArchiveSport): number {
    return item.id!;
  }

  delete(archiveSport: IArchiveSport): void {
    const modalRef = this.modalService.open(ArchiveSportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.archiveSport = archiveSport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
