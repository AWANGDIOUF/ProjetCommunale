import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecuperationRecyclable } from '../recuperation-recyclable.model';

@Component({
  selector: 'jhi-recuperation-recyclable-detail',
  templateUrl: './recuperation-recyclable-detail.component.html',
})
export class RecuperationRecyclableDetailComponent implements OnInit {
  recuperationRecyclable: IRecuperationRecyclable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recuperationRecyclable }) => {
      this.recuperationRecyclable = recuperationRecyclable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
