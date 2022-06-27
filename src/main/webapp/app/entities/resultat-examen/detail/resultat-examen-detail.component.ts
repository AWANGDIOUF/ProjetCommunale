import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultatExamen } from '../resultat-examen.model';

@Component({
  selector: 'jhi-resultat-examen-detail',
  templateUrl: './resultat-examen-detail.component.html',
})
export class ResultatExamenDetailComponent implements OnInit {
  resultatExamen: IResultatExamen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultatExamen }) => {
      this.resultatExamen = resultatExamen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
