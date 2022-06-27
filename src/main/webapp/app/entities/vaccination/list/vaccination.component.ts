import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVaccination } from '../vaccination.model';
import { VaccinationService } from '../service/vaccination.service';
import { VaccinationDeleteDialogComponent } from '../delete/vaccination-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-vaccination',
  templateUrl: './vaccination.component.html',
})
export class VaccinationComponent implements OnInit {
  vaccinations?: IVaccination[];
  isLoading = false;

  constructor(protected vaccinationService: VaccinationService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vaccinationService.query().subscribe({
      next: (res: HttpResponse<IVaccination[]>) => {
        this.isLoading = false;
        this.vaccinations = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IVaccination): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(vaccination: IVaccination): void {
    const modalRef = this.modalService.open(VaccinationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vaccination = vaccination;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
