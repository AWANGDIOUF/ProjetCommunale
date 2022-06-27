import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonneur } from '../donneur.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-donneur-detail',
  templateUrl: './donneur-detail.component.html',
})
export class DonneurDetailComponent implements OnInit {
  donneur: IDonneur | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donneur }) => {
      this.donneur = donneur;
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
