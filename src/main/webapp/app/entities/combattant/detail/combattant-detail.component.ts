import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICombattant } from '../combattant.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-combattant-detail',
  templateUrl: './combattant-detail.component.html',
})
export class CombattantDetailComponent implements OnInit {
  combattant: ICombattant | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ combattant }) => {
      this.combattant = combattant;
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
