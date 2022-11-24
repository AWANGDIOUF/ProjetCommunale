import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArchiveSport } from '../archive-sport.model';

@Component({
  selector: 'jhi-archive-sport-detail',
  templateUrl: './archive-sport-detail.component.html',
})
export class ArchiveSportDetailComponent implements OnInit {
  archiveSport: IArchiveSport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ archiveSport }) => {
      this.archiveSport = archiveSport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
