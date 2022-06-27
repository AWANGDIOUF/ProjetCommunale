import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProposition } from '../proposition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-proposition-detail',
  templateUrl: './proposition-detail.component.html',
})
export class PropositionDetailComponent implements OnInit {
  proposition: IProposition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposition }) => {
      this.proposition = proposition;
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
