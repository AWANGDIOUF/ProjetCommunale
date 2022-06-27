import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDomaineActivite } from '../domaine-activite.model';

@Component({
  selector: 'jhi-domaine-activite-detail',
  templateUrl: './domaine-activite-detail.component.html',
})
export class DomaineActiviteDetailComponent implements OnInit {
  domaineActivite: IDomaineActivite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaineActivite }) => {
      this.domaineActivite = domaineActivite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
