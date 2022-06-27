import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILienTutoriel } from '../lien-tutoriel.model';

@Component({
  selector: 'jhi-lien-tutoriel-detail',
  templateUrl: './lien-tutoriel-detail.component.html',
})
export class LienTutorielDetailComponent implements OnInit {
  lienTutoriel: ILienTutoriel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lienTutoriel }) => {
      this.lienTutoriel = lienTutoriel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
