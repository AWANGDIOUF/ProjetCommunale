import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeVaccin } from '../type-vaccin.model';

@Component({
  selector: 'jhi-type-vaccin-detail',
  templateUrl: './type-vaccin-detail.component.html',
})
export class TypeVaccinDetailComponent implements OnInit {
  typeVaccin: ITypeVaccin | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeVaccin }) => {
      this.typeVaccin = typeVaccin;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
