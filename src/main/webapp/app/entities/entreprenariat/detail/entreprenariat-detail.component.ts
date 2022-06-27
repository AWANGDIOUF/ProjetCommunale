import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntreprenariat } from '../entreprenariat.model';

@Component({
  selector: 'jhi-entreprenariat-detail',
  templateUrl: './entreprenariat-detail.component.html',
})
export class EntreprenariatDetailComponent implements OnInit {
  entreprenariat: IEntreprenariat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreprenariat }) => {
      this.entreprenariat = entreprenariat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
