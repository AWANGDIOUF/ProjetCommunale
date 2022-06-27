import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVidange, Vidange } from '../vidange.model';
import { VidangeService } from '../service/vidange.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

@Component({
  selector: 'jhi-vidange-update',
  templateUrl: './vidange-update.component.html',
})
export class VidangeUpdateComponent implements OnInit {
  isSaving = false;

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomVideur: [],
    prenomVideur: [],
    tel1: [],
    tel2: [],
    quartier: [],
  });

  constructor(
    protected vidangeService: VidangeService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vidange }) => {
      this.updateForm(vidange);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vidange = this.createFromForm();
    if (vidange.id !== undefined) {
      this.subscribeToSaveResponse(this.vidangeService.update(vidange));
    } else {
      this.subscribeToSaveResponse(this.vidangeService.create(vidange));
    }
  }

  trackQuartierById(_index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVidange>>): void {
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

  protected updateForm(vidange: IVidange): void {
    this.editForm.patchValue({
      id: vidange.id,
      nomVideur: vidange.nomVideur,
      prenomVideur: vidange.prenomVideur,
      tel1: vidange.tel1,
      tel2: vidange.tel2,
      quartier: vidange.quartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      vidange.quartier
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

  protected createFromForm(): IVidange {
    return {
      ...new Vidange(),
      id: this.editForm.get(['id'])!.value,
      nomVideur: this.editForm.get(['nomVideur'])!.value,
      prenomVideur: this.editForm.get(['prenomVideur'])!.value,
      tel1: this.editForm.get(['tel1'])!.value,
      tel2: this.editForm.get(['tel2'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
    };
  }
}
