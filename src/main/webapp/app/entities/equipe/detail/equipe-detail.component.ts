import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipe } from '../equipe.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-equipe-detail',
  templateUrl: './equipe-detail.component.html',
})
export class EquipeDetailComponent implements OnInit {
  equipe: IEquipe | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipe }) => {
      this.equipe = equipe;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
