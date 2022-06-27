import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEleveur } from '../eleveur.model';

@Component({
  selector: 'jhi-eleveur-detail',
  templateUrl: './eleveur-detail.component.html',
})
export class EleveurDetailComponent implements OnInit {
  eleveur: IEleveur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eleveur }) => {
      this.eleveur = eleveur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
