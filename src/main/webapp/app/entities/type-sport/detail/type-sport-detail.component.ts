import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeSport } from '../type-sport.model';

@Component({
  selector: 'jhi-type-sport-detail',
  templateUrl: './type-sport-detail.component.html',
})
export class TypeSportDetailComponent implements OnInit {
  typeSport: ITypeSport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSport }) => {
      this.typeSport = typeSport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
