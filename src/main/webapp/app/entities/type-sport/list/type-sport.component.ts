import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeSport } from '../type-sport.model';
import { TypeSportService } from '../service/type-sport.service';
import { TypeSportDeleteDialogComponent } from '../delete/type-sport-delete-dialog.component';

@Component({
  selector: 'jhi-type-sport',
  templateUrl: './type-sport.component.html',
})
export class TypeSportComponent implements OnInit {
  typeSports?: ITypeSport[];
  isLoading = false;

  constructor(protected typeSportService: TypeSportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typeSportService.query().subscribe({
      next: (res: HttpResponse<ITypeSport[]>) => {
        this.isLoading = false;
        this.typeSports = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITypeSport): number {
    return item.id!;
  }

  delete(typeSport: ITypeSport): void {
    const modalRef = this.modalService.open(TypeSportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typeSport = typeSport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
