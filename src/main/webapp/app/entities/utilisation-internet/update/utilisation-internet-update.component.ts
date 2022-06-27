import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUtilisationInternet, UtilisationInternet } from '../utilisation-internet.model';
import { UtilisationInternetService } from '../service/utilisation-internet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ILogiciel } from 'app/entities/logiciel/logiciel.model';
import { LogicielService } from 'app/entities/logiciel/service/logiciel.service';
import { Profil } from 'app/entities/enumerations/profil.model';
import { Domaine } from 'app/entities/enumerations/domaine.model';

@Component({
  selector: 'jhi-utilisation-internet-update',
  templateUrl: './utilisation-internet-update.component.html',
})
export class UtilisationInternetUpdateComponent implements OnInit {
  isSaving = false;
  profilValues = Object.keys(Profil);
  domaineValues = Object.keys(Domaine);

  logicielsCollection: ILogiciel[] = [];

  editForm = this.fb.group({
    id: [],
    profil: [],
    autre: [],
    domaine: [],
    description: [],
    logiciel: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected utilisationInternetService: UtilisationInternetService,
    protected logicielService: LogicielService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisationInternet }) => {
      this.updateForm(utilisationInternet);

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
    const utilisationInternet = this.createFromForm();
    if (utilisationInternet.id !== undefined) {
      this.subscribeToSaveResponse(this.utilisationInternetService.update(utilisationInternet));
    } else {
      this.subscribeToSaveResponse(this.utilisationInternetService.create(utilisationInternet));
    }
  }

  trackLogicielById(_index: number, item: ILogiciel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisationInternet>>): void {
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

  protected updateForm(utilisationInternet: IUtilisationInternet): void {
    this.editForm.patchValue({
      id: utilisationInternet.id,
      profil: utilisationInternet.profil,
      autre: utilisationInternet.autre,
      domaine: utilisationInternet.domaine,
      description: utilisationInternet.description,
      logiciel: utilisationInternet.logiciel,
    });

    this.logicielsCollection = this.logicielService.addLogicielToCollectionIfMissing(
      this.logicielsCollection,
      utilisationInternet.logiciel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.logicielService
      .query({ filter: 'utilisationinternet-is-null' })
      .pipe(map((res: HttpResponse<ILogiciel[]>) => res.body ?? []))
      .pipe(
        map((logiciels: ILogiciel[]) =>
          this.logicielService.addLogicielToCollectionIfMissing(logiciels, this.editForm.get('logiciel')!.value)
        )
      )
      .subscribe((logiciels: ILogiciel[]) => (this.logicielsCollection = logiciels));
  }

  protected createFromForm(): IUtilisationInternet {
    return {
      ...new UtilisationInternet(),
      id: this.editForm.get(['id'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      autre: this.editForm.get(['autre'])!.value,
      domaine: this.editForm.get(['domaine'])!.value,
      description: this.editForm.get(['description'])!.value,
      logiciel: this.editForm.get(['logiciel'])!.value,
    };
  }
}
