import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISensibiisationInternet, SensibiisationInternet } from '../sensibiisation-internet.model';
import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sensibiisation-internet-update',
  templateUrl: './sensibiisation-internet-update.component.html',
})
export class SensibiisationInternetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    theme: [],
    interdiction: [],
    bonnePratique: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sensibiisationInternetService: SensibiisationInternetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensibiisationInternet }) => {
      this.updateForm(sensibiisationInternet);
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
    const sensibiisationInternet = this.createFromForm();
    if (sensibiisationInternet.id !== undefined) {
      this.subscribeToSaveResponse(this.sensibiisationInternetService.update(sensibiisationInternet));
    } else {
      this.subscribeToSaveResponse(this.sensibiisationInternetService.create(sensibiisationInternet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensibiisationInternet>>): void {
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

  protected updateForm(sensibiisationInternet: ISensibiisationInternet): void {
    this.editForm.patchValue({
      id: sensibiisationInternet.id,
      theme: sensibiisationInternet.theme,
      interdiction: sensibiisationInternet.interdiction,
      bonnePratique: sensibiisationInternet.bonnePratique,
    });
  }

  protected createFromForm(): ISensibiisationInternet {
    return {
      ...new SensibiisationInternet(),
      id: this.editForm.get(['id'])!.value,
      theme: this.editForm.get(['theme'])!.value,
      interdiction: this.editForm.get(['interdiction'])!.value,
      bonnePratique: this.editForm.get(['bonnePratique'])!.value,
    };
  }
}
