import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IActivitePolitique, ActivitePolitique } from '../activite-politique.model';
import { ActivitePolitiqueService } from '../service/activite-politique.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-activite-politique-update',
  templateUrl: './activite-politique-update.component.html',
})
export class ActivitePolitiqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    titreActivite: [],
    descriptionActivite: [],
    dateDebut: [],
    dateFin: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected activitePolitiqueService: ActivitePolitiqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activitePolitique }) => {
      this.updateForm(activitePolitique);
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
    const activitePolitique = this.createFromForm();
    if (activitePolitique.id !== undefined) {
      this.subscribeToSaveResponse(this.activitePolitiqueService.update(activitePolitique));
    } else {
      this.subscribeToSaveResponse(this.activitePolitiqueService.create(activitePolitique));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivitePolitique>>): void {
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

  protected updateForm(activitePolitique: IActivitePolitique): void {
    this.editForm.patchValue({
      id: activitePolitique.id,
      titreActivite: activitePolitique.titreActivite,
      descriptionActivite: activitePolitique.descriptionActivite,
      dateDebut: activitePolitique.dateDebut,
      dateFin: activitePolitique.dateFin,
    });
  }

  protected createFromForm(): IActivitePolitique {
    return {
      ...new ActivitePolitique(),
      id: this.editForm.get(['id'])!.value,
      titreActivite: this.editForm.get(['titreActivite'])!.value,
      descriptionActivite: this.editForm.get(['descriptionActivite'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
    };
  }
}
