import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVainqueur } from '../vainqueur.model';

@Component({
  selector: 'jhi-vainqueur-detail',
  templateUrl: './vainqueur-detail.component.html',
})
export class VainqueurDetailComponent implements OnInit {
  vainqueur: IVainqueur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vainqueur }) => {
      this.vainqueur = vainqueur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
