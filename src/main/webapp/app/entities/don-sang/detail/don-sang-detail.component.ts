import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonSang } from '../don-sang.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-don-sang-detail',
  templateUrl: './don-sang-detail.component.html',
})
export class DonSangDetailComponent implements OnInit {
  donSang: IDonSang | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donSang }) => {
      this.donSang = donSang;
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
