import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILogiciel } from '../logiciel.model';
import { LogicielService } from '../service/logiciel.service';
import { LogicielDeleteDialogComponent } from '../delete/logiciel-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-logiciel',
  templateUrl: './logiciel.component.html',
})
export class LogicielComponent implements OnInit {
  logiciels?: ILogiciel[];
  isLoading = false;

  constructor(protected logicielService: LogicielService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.logicielService.query().subscribe({
      next: (res: HttpResponse<ILogiciel[]>) => {
        this.isLoading = false;
        this.logiciels = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILogiciel): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(logiciel: ILogiciel): void {
    const modalRef = this.modalService.open(LogicielDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.logiciel = logiciel;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
