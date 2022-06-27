import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalendrierEvenement } from '../calendrier-evenement.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-calendrier-evenement-detail',
  templateUrl: './calendrier-evenement-detail.component.html',
})
export class CalendrierEvenementDetailComponent implements OnInit {
  calendrierEvenement: ICalendrierEvenement | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendrierEvenement }) => {
      this.calendrierEvenement = calendrierEvenement;
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
