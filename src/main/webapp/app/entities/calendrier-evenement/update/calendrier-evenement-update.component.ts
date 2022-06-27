import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICalendrierEvenement, CalendrierEvenement } from '../calendrier-evenement.model';
import { CalendrierEvenementService } from '../service/calendrier-evenement.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-calendrier-evenement-update',
  templateUrl: './calendrier-evenement-update.component.html',
})
export class CalendrierEvenementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomEve: [],
    but: [],
    objectif: [],
    date: [],
    lieu: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected calendrierEvenementService: CalendrierEvenementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendrierEvenement }) => {
      this.updateForm(calendrierEvenement);
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
    const calendrierEvenement = this.createFromForm();
    if (calendrierEvenement.id !== undefined) {
      this.subscribeToSaveResponse(this.calendrierEvenementService.update(calendrierEvenement));
    } else {
      this.subscribeToSaveResponse(this.calendrierEvenementService.create(calendrierEvenement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalendrierEvenement>>): void {
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

  protected updateForm(calendrierEvenement: ICalendrierEvenement): void {
    this.editForm.patchValue({
      id: calendrierEvenement.id,
      nomEve: calendrierEvenement.nomEve,
      but: calendrierEvenement.but,
      objectif: calendrierEvenement.objectif,
      date: calendrierEvenement.date,
      lieu: calendrierEvenement.lieu,
    });
  }

  protected createFromForm(): ICalendrierEvenement {
    return {
      ...new CalendrierEvenement(),
      id: this.editForm.get(['id'])!.value,
      nomEve: this.editForm.get(['nomEve'])!.value,
      but: this.editForm.get(['but'])!.value,
      objectif: this.editForm.get(['objectif'])!.value,
      date: this.editForm.get(['date'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
    };
  }
}
