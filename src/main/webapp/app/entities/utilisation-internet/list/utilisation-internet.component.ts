import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilisationInternet } from '../utilisation-internet.model';
import { UtilisationInternetService } from '../service/utilisation-internet.service';
import { UtilisationInternetDeleteDialogComponent } from '../delete/utilisation-internet-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-utilisation-internet',
  templateUrl: './utilisation-internet.component.html',
})
export class UtilisationInternetComponent implements OnInit {
  utilisationInternets?: IUtilisationInternet[];
  isLoading = false;

  constructor(
    protected utilisationInternetService: UtilisationInternetService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.utilisationInternetService.query().subscribe({
      next: (res: HttpResponse<IUtilisationInternet[]>) => {
        this.isLoading = false;
        this.utilisationInternets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IUtilisationInternet): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(utilisationInternet: IUtilisationInternet): void {
    const modalRef = this.modalService.open(UtilisationInternetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.utilisationInternet = utilisationInternet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
