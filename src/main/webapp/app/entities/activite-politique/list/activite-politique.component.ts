import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivitePolitique } from '../activite-politique.model';
import { ActivitePolitiqueService } from '../service/activite-politique.service';
import { ActivitePolitiqueDeleteDialogComponent } from '../delete/activite-politique-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-activite-politique',
  templateUrl: './activite-politique.component.html',
})
export class ActivitePolitiqueComponent implements OnInit {
  activitePolitiques?: IActivitePolitique[];
  isLoading = false;

  constructor(
    protected activitePolitiqueService: ActivitePolitiqueService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.activitePolitiqueService.query().subscribe({
      next: (res: HttpResponse<IActivitePolitique[]>) => {
        this.isLoading = false;
        this.activitePolitiques = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IActivitePolitique): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(activitePolitique: IActivitePolitique): void {
    const modalRef = this.modalService.open(ActivitePolitiqueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.activitePolitique = activitePolitique;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
