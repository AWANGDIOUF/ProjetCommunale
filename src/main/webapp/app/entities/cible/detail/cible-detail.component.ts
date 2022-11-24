import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICible } from '../cible.model';

@Component({
  selector: 'jhi-cible-detail',
  templateUrl: './cible-detail.component.html',
})
export class CibleDetailComponent implements OnInit {
  cible: ICible | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cible }) => {
      this.cible = cible;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
