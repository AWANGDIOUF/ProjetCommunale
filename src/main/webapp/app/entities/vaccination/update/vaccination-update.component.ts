import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVaccination, Vaccination } from '../vaccination.model';
import { VaccinationService } from '../service/vaccination.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

@Component({
  selector: 'jhi-vaccination-update',
  templateUrl: './vaccination-update.component.html',
})
export class VaccinationUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    description: [],
    duree: [],
    dateDebut: [],
    dateFin: [],
    annonce: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected vaccinationService: VaccinationService,
    protected annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaccination }) => {
      this.updateForm(vaccination);

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
        this.eventManager.broadcast(new EventWithContent<AlertError>('projetCommunalApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaccination = this.createFromForm();
    if (vaccination.id !== undefined) {
      this.subscribeToSaveResponse(this.vaccinationService.update(vaccination));
    } else {
      this.subscribeToSaveResponse(this.vaccinationService.create(vaccination));
    }
  }

  trackAnnonceById(index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaccination>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(vaccination: IVaccination): void {
    this.editForm.patchValue({
      id: vaccination.id,
      date: vaccination.date,
      description: vaccination.description,
      duree: vaccination.duree,
      dateDebut: vaccination.dateDebut,
      dateFin: vaccination.dateFin,
      annonce: vaccination.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, vaccination.annonce);
  }

  protected loadRelationshipsOptions(): void {
    this.annonceService
      .query()
      .pipe(map((res: HttpResponse<IAnnonce[]>) => res.body ?? []))
      .pipe(
        map((annonces: IAnnonce[]) => this.annonceService.addAnnonceToCollectionIfMissing(annonces, this.editForm.get('annonce')!.value))
      )
      .subscribe((annonces: IAnnonce[]) => (this.annoncesSharedCollection = annonces));
  }

  protected createFromForm(): IVaccination {
    return {
      ...new Vaccination(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      description: this.editForm.get(['description'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      annonce: this.editForm.get(['annonce'])!.value,
    };
  }
}
