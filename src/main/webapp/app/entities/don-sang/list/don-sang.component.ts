import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonSang } from '../don-sang.model';
import { DonSangService } from '../service/don-sang.service';
import { DonSangDeleteDialogComponent } from '../delete/don-sang-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-don-sang',
  templateUrl: './don-sang.component.html',
})
export class DonSangComponent implements OnInit {
  donSangs?: IDonSang[];
  isLoading = false;

  constructor(protected donSangService: DonSangService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.donSangService.query().subscribe({
      next: (res: HttpResponse<IDonSang[]>) => {
        this.isLoading = false;
        this.donSangs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDonSang): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(donSang: IDonSang): void {
    const modalRef = this.modalService.open(DonSangDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.donSang = donSang;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
