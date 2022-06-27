import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonneur } from '../donneur.model';
import { DonneurService } from '../service/donneur.service';
import { DonneurDeleteDialogComponent } from '../delete/donneur-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-donneur',
  templateUrl: './donneur.component.html',
})
export class DonneurComponent implements OnInit {
  donneurs?: IDonneur[];
  isLoading = false;

  constructor(protected donneurService: DonneurService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.donneurService.query().subscribe({
      next: (res: HttpResponse<IDonneur[]>) => {
        this.isLoading = false;
        this.donneurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDonneur): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(donneur: IDonneur): void {
    const modalRef = this.modalService.open(DonneurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.donneur = donneur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
