import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuartier, Quartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

@Component({
  selector: 'jhi-quartier-update',
  templateUrl: './quartier-update.component.html',
})
export class QuartierUpdateComponent implements OnInit {
  isSaving = false;

  artistesCollection: IArtiste[] = [];

  editForm = this.fb.group({
    id: [],
    nomQuartier: [],
    artiste: [],
  });

  constructor(
    protected quartierService: QuartierService,
    protected artisteService: ArtisteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quartier }) => {
      this.updateForm(quartier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quartier = this.createFromForm();
    if (quartier.id !== undefined) {
      this.subscribeToSaveResponse(this.quartierService.update(quartier));
    } else {
      this.subscribeToSaveResponse(this.quartierService.create(quartier));
    }
  }

  trackArtisteById(_index: number, item: IArtiste): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuartier>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(quartier: IQuartier): void {
    this.editForm.patchValue({
      id: quartier.id,
      nomQuartier: quartier.nomQuartier,
      artiste: quartier.artiste,
    });

    this.artistesCollection = this.artisteService.addArtisteToCollectionIfMissing(this.artistesCollection, quartier.artiste);
  }

  protected loadRelationshipsOptions(): void {
    this.artisteService
      .query({ filter: 'quartier-is-null' })
      .pipe(map((res: HttpResponse<IArtiste[]>) => res.body ?? []))
      .pipe(
        map((artistes: IArtiste[]) => this.artisteService.addArtisteToCollectionIfMissing(artistes, this.editForm.get('artiste')!.value))
      )
      .subscribe((artistes: IArtiste[]) => (this.artistesCollection = artistes));
  }

  protected createFromForm(): IQuartier {
    return {
      ...new Quartier(),
      id: this.editForm.get(['id'])!.value,
      nomQuartier: this.editForm.get(['nomQuartier'])!.value,
      artiste: this.editForm.get(['artiste'])!.value,
    };
  }
}
