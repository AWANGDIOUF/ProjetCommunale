import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtablissement } from '../etablissement.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-etablissement-detail',
  templateUrl: './etablissement-detail.component.html',
})
export class EtablissementDetailComponent implements OnInit {
  etablissement: IEtablissement | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.etablissement = etablissement;
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
