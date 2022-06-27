import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRecuperationRecyclable, RecuperationRecyclable } from '../recuperation-recyclable.model';
import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

@Component({
  selector: 'jhi-recuperation-recyclable-update',
  templateUrl: './recuperation-recyclable-update.component.html',
})
export class RecuperationRecyclableUpdateComponent implements OnInit {
  isSaving = false;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    date: [],
    lieu: [],
    quartier: [],
  });

  constructor(
    protected recuperationRecyclableService: RecuperationRecyclableService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recuperationRecyclable }) => {
      this.updateForm(recuperationRecyclable);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recuperationRecyclable = this.createFromForm();
    if (recuperationRecyclable.id !== undefined) {
      this.subscribeToSaveResponse(this.recuperationRecyclableService.update(recuperationRecyclable));
    } else {
      this.subscribeToSaveResponse(this.recuperationRecyclableService.create(recuperationRecyclable));
    }
  }

  trackQuartierById(_index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecuperationRecyclable>>): void {
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

  protected updateForm(recuperationRecyclable: IRecuperationRecyclable): void {
    this.editForm.patchValue({
      id: recuperationRecyclable.id,
      nom: recuperationRecyclable.nom,
      date: recuperationRecyclable.date,
      lieu: recuperationRecyclable.lieu,
      quartier: recuperationRecyclable.quartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      recuperationRecyclable.quartier
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

  protected createFromForm(): IRecuperationRecyclable {
    return {
      ...new RecuperationRecyclable(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      date: this.editForm.get(['date'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
    };
  }
}
