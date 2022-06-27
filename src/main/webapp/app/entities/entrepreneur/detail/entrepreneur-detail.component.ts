import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntrepreneur } from '../entrepreneur.model';

@Component({
  selector: 'jhi-entrepreneur-detail',
  templateUrl: './entrepreneur-detail.component.html',
})
export class EntrepreneurDetailComponent implements OnInit {
  entrepreneur: IEntrepreneur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entrepreneur }) => {
      this.entrepreneur = entrepreneur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
