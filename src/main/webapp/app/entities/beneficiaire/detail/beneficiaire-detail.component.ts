import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeneficiaire } from '../beneficiaire.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-beneficiaire-detail',
  templateUrl: './beneficiaire-detail.component.html',
})
export class BeneficiaireDetailComponent implements OnInit {
  beneficiaire: IBeneficiaire | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiaire }) => {
      this.beneficiaire = beneficiaire;
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
