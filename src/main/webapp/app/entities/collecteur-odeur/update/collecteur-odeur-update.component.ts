import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICollecteurOdeur, CollecteurOdeur } from '../collecteur-odeur.model';
import { CollecteurOdeurService } from '../service/collecteur-odeur.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

@Component({
  selector: 'jhi-collecteur-odeur-update',
  templateUrl: './collecteur-odeur-update.component.html',
})
export class CollecteurOdeurUpdateComponent implements OnInit {
  isSaving = false;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomCollecteur: [],
    prenomCollecteur: [],
    date: [],
    tel1: [],
    quartier: [],
  });

  constructor(
    protected collecteurOdeurService: CollecteurOdeurService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collecteurOdeur }) => {
      this.updateForm(collecteurOdeur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const collecteurOdeur = this.createFromForm();
    if (collecteurOdeur.id !== undefined) {
      this.subscribeToSaveResponse(this.collecteurOdeurService.update(collecteurOdeur));
    } else {
      this.subscribeToSaveResponse(this.collecteurOdeurService.create(collecteurOdeur));
    }
  }

  trackQuartierById(_index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollecteurOdeur>>): void {
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

  protected updateForm(collecteurOdeur: ICollecteurOdeur): void {
    this.editForm.patchValue({
      id: collecteurOdeur.id,
      nomCollecteur: collecteurOdeur.nomCollecteur,
      prenomCollecteur: collecteurOdeur.prenomCollecteur,
      date: collecteurOdeur.date,
      tel1: collecteurOdeur.tel1,
      quartier: collecteurOdeur.quartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      collecteurOdeur.quartier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('quartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));
  }

  protected createFromForm(): ICollecteurOdeur {
    return {
      ...new CollecteurOdeur(),
      id: this.editForm.get(['id'])!.value,
      nomCollecteur: this.editForm.get(['nomCollecteur'])!.value,
      prenomCollecteur: this.editForm.get(['prenomCollecteur'])!.value,
      date: this.editForm.get(['date'])!.value,
      tel1: this.editForm.get(['tel1'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
    };
  }
}
