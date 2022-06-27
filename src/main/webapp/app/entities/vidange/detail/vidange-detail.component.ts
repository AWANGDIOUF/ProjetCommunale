import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVidange } from '../vidange.model';

@Component({
  selector: 'jhi-vidange-detail',
  templateUrl: './vidange-detail.component.html',
})
export class VidangeDetailComponent implements OnInit {
  vidange: IVidange | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vidange }) => {
      this.vidange = vidange;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
