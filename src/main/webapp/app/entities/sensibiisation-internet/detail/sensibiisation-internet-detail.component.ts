import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISensibiisationInternet } from '../sensibiisation-internet.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sensibiisation-internet-detail',
  templateUrl: './sensibiisation-internet-detail.component.html',
})
export class SensibiisationInternetDetailComponent implements OnInit {
  sensibiisationInternet: ISensibiisationInternet | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensibiisationInternet }) => {
      this.sensibiisationInternet = sensibiisationInternet;
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
