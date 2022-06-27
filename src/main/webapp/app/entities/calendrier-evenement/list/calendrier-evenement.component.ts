import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICalendrierEvenement } from '../calendrier-evenement.model';
import { CalendrierEvenementService } from '../service/calendrier-evenement.service';
import { CalendrierEvenementDeleteDialogComponent } from '../delete/calendrier-evenement-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-calendrier-evenement',
  templateUrl: './calendrier-evenement.component.html',
})
export class CalendrierEvenementComponent implements OnInit {
  calendrierEvenements?: ICalendrierEvenement[];
  isLoading = false;

  constructor(
    protected calendrierEvenementService: CalendrierEvenementService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.calendrierEvenementService.query().subscribe({
      next: (res: HttpResponse<ICalendrierEvenement[]>) => {
        this.isLoading = false;
        this.calendrierEvenements = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICalendrierEvenement): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(calendrierEvenement: ICalendrierEvenement): void {
    const modalRef = this.modalService.open(CalendrierEvenementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.calendrierEvenement = calendrierEvenement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
