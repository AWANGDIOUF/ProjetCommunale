import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';
import { TypeVaccinDeleteDialogComponent } from '../delete/type-vaccin-delete-dialog.component';

@Component({
  selector: 'jhi-type-vaccin',
  templateUrl: './type-vaccin.component.html',
})
export class TypeVaccinComponent implements OnInit {
  typeVaccins?: ITypeVaccin[];
  isLoading = false;

  constructor(protected typeVaccinService: TypeVaccinService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.typeVaccinService.query().subscribe({
      next: (res: HttpResponse<ITypeVaccin[]>) => {
        this.isLoading = false;
        this.typeVaccins = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITypeVaccin): number {
    return item.id!;
  }

  delete(typeVaccin: ITypeVaccin): void {
    const modalRef = this.modalService.open(TypeVaccinDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.typeVaccin = typeVaccin;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
