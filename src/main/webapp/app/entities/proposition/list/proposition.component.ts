import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposition } from '../proposition.model';
import { PropositionService } from '../service/proposition.service';
import { PropositionDeleteDialogComponent } from '../delete/proposition-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-proposition',
  templateUrl: './proposition.component.html',
})
export class PropositionComponent implements OnInit {
  propositions?: IProposition[];
  isLoading = false;

  constructor(protected propositionService: PropositionService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.propositionService.query().subscribe({
      next: (res: HttpResponse<IProposition[]>) => {
        this.isLoading = false;
        this.propositions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProposition): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(proposition: IProposition): void {
    const modalRef = this.modalService.open(PropositionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proposition = proposition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
