import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVaccination } from '../vaccination.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-vaccination-detail',
  templateUrl: './vaccination-detail.component.html',
})
export class VaccinationDetailComponent implements OnInit {
  vaccination: IVaccination | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaccination }) => {
      this.vaccination = vaccination;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
