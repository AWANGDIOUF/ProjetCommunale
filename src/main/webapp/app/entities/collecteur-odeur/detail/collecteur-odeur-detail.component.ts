import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollecteurOdeur } from '../collecteur-odeur.model';

@Component({
  selector: 'jhi-collecteur-odeur-detail',
  templateUrl: './collecteur-odeur-detail.component.html',
})
export class CollecteurOdeurDetailComponent implements OnInit {
  collecteurOdeur: ICollecteurOdeur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collecteurOdeur }) => {
      this.collecteurOdeur = collecteurOdeur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
