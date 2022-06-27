import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProposition, Proposition } from '../proposition.model';
import { PropositionService } from '../service/proposition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';
import { EnsegnantService } from 'app/entities/ensegnant/service/ensegnant.service';

@Component({
  selector: 'jhi-proposition-update',
  templateUrl: './proposition-update.component.html',
})
export class PropositionUpdateComponent implements OnInit {
  isSaving = false;

  ensegnantsSharedCollection: IEnsegnant[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    enseignant: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected propositionService: PropositionService,
    protected ensegnantService: EnsegnantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposition }) => {
      this.updateForm(proposition);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('projetCommunaleApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proposition = this.createFromForm();
    if (proposition.id !== undefined) {
      this.subscribeToSaveResponse(this.propositionService.update(proposition));
    } else {
      this.subscribeToSaveResponse(this.propositionService.create(proposition));
    }
  }

  trackEnsegnantById(_index: number, item: IEnsegnant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposition>>): void {
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

  protected updateForm(proposition: IProposition): void {
    this.editForm.patchValue({
      id: proposition.id,
      description: proposition.description,
      enseignant: proposition.enseignant,
    });

    this.ensegnantsSharedCollection = this.ensegnantService.addEnsegnantToCollectionIfMissing(
      this.ensegnantsSharedCollection,
      proposition.enseignant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ensegnantService
      .query()
      .pipe(map((res: HttpResponse<IEnsegnant[]>) => res.body ?? []))
      .pipe(
        map((ensegnants: IEnsegnant[]) =>
          this.ensegnantService.addEnsegnantToCollectionIfMissing(ensegnants, this.editForm.get('enseignant')!.value)
        )
      )
      .subscribe((ensegnants: IEnsegnant[]) => (this.ensegnantsSharedCollection = ensegnants));
  }

  protected createFromForm(): IProposition {
    return {
      ...new Proposition(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      enseignant: this.editForm.get(['enseignant'])!.value,
    };
  }
}
