import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDonSang, DonSang } from '../don-sang.model';
import { DonSangService } from '../service/don-sang.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';
import { IDonneur } from 'app/entities/donneur/donneur.model';
import { DonneurService } from 'app/entities/donneur/service/donneur.service';

@Component({
  selector: 'jhi-don-sang-update',
  templateUrl: './don-sang-update.component.html',
})
export class DonSangUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];
  donneursSharedCollection: IDonneur[] = [];

  editForm = this.fb.group({
    id: [],
    organisateur: [],
    description: [],
    annonce: [],
    donneur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected donSangService: DonSangService,
    protected annonceService: AnnonceService,
    protected donneurService: DonneurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donSang }) => {
      this.updateForm(donSang);

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
    const donSang = this.createFromForm();
    if (donSang.id !== undefined) {
      this.subscribeToSaveResponse(this.donSangService.update(donSang));
    } else {
      this.subscribeToSaveResponse(this.donSangService.create(donSang));
    }
  }

  trackAnnonceById(_index: number, item: IAnnonce): number {
    return item.id!;
  }

  trackDonneurById(_index: number, item: IDonneur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonSang>>): void {
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

  protected updateForm(donSang: IDonSang): void {
    this.editForm.patchValue({
      id: donSang.id,
      organisateur: donSang.organisateur,
      description: donSang.description,
      annonce: donSang.annonce,
      donneur: donSang.donneur,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, donSang.annonce);
    this.donneursSharedCollection = this.donneurService.addDonneurToCollectionIfMissing(this.donneursSharedCollection, donSang.donneur);
  }

  protected loadRelationshipsOptions(): void {
    this.annonceService
      .query()
      .pipe(map((res: HttpResponse<IAnnonce[]>) => res.body ?? []))
      .pipe(
        map((annonces: IAnnonce[]) => this.annonceService.addAnnonceToCollectionIfMissing(annonces, this.editForm.get('annonce')!.value))
      )
      .subscribe((annonces: IAnnonce[]) => (this.annoncesSharedCollection = annonces));

    this.donneurService
      .query()
      .pipe(map((res: HttpResponse<IDonneur[]>) => res.body ?? []))
      .pipe(
        map((donneurs: IDonneur[]) => this.donneurService.addDonneurToCollectionIfMissing(donneurs, this.editForm.get('donneur')!.value))
      )
      .subscribe((donneurs: IDonneur[]) => (this.donneursSharedCollection = donneurs));
  }

  protected createFromForm(): IDonSang {
    return {
      ...new DonSang(),
      id: this.editForm.get(['id'])!.value,
      organisateur: this.editForm.get(['organisateur'])!.value,
      description: this.editForm.get(['description'])!.value,
      annonce: this.editForm.get(['annonce'])!.value,
      donneur: this.editForm.get(['donneur'])!.value,
    };
  }
}
