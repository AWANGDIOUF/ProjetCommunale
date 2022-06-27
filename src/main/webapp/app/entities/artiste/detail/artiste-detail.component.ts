import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArtiste } from '../artiste.model';

@Component({
  selector: 'jhi-artiste-detail',
  templateUrl: './artiste-detail.component.html',
})
export class ArtisteDetailComponent implements OnInit {
  artiste: IArtiste | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artiste }) => {
      this.artiste = artiste;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
