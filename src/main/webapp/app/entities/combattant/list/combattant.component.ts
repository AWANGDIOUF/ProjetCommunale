import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICombattant } from '../combattant.model';
import { CombattantService } from '../service/combattant.service';
import { CombattantDeleteDialogComponent } from '../delete/combattant-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-combattant',
  templateUrl: './combattant.component.html',
})
export class CombattantComponent implements OnInit {
  combattants?: ICombattant[];
  isLoading = false;

  constructor(protected combattantService: CombattantService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.combattantService.query().subscribe({
      next: (res: HttpResponse<ICombattant[]>) => {
        this.isLoading = false;
        this.combattants = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICombattant): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(combattant: ICombattant): void {
    const modalRef = this.modalService.open(CombattantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.combattant = combattant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
