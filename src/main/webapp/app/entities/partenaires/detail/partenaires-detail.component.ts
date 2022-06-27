import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartenaires } from '../partenaires.model';

@Component({
  selector: 'jhi-partenaires-detail',
  templateUrl: './partenaires-detail.component.html',
})
export class PartenairesDetailComponent implements OnInit {
  partenaires: IPartenaires | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partenaires }) => {
      this.partenaires = partenaires;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
