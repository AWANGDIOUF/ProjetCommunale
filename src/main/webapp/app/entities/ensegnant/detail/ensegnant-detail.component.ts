import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnsegnant } from '../ensegnant.model';

@Component({
  selector: 'jhi-ensegnant-detail',
  templateUrl: './ensegnant-detail.component.html',
})
export class EnsegnantDetailComponent implements OnInit {
  ensegnant: IEnsegnant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ensegnant }) => {
      this.ensegnant = ensegnant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
