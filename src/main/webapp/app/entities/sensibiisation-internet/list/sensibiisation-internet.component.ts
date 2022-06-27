import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensibiisationInternet } from '../sensibiisation-internet.model';
import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';
import { SensibiisationInternetDeleteDialogComponent } from '../delete/sensibiisation-internet-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sensibiisation-internet',
  templateUrl: './sensibiisation-internet.component.html',
})
export class SensibiisationInternetComponent implements OnInit {
  sensibiisationInternets?: ISensibiisationInternet[];
  isLoading = false;

  constructor(
    protected sensibiisationInternetService: SensibiisationInternetService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensibiisationInternetService.query().subscribe({
      next: (res: HttpResponse<ISensibiisationInternet[]>) => {
        this.isLoading = false;
        this.sensibiisationInternets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensibiisationInternet): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(sensibiisationInternet: ISensibiisationInternet): void {
    const modalRef = this.modalService.open(SensibiisationInternetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensibiisationInternet = sensibiisationInternet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
