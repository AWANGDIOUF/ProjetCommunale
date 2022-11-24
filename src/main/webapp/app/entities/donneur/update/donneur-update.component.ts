import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDonneur, Donneur } from '../donneur.model';
import { DonneurService } from '../service/donneur.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDon } from 'app/entities/don/don.model';
import { DonService } from 'app/entities/don/service/don.service';

@Component({
  selector: 'jhi-donneur-update',
  templateUrl: './donneur-update.component.html',
})
export class DonneurUpdateComponent implements OnInit {
  isSaving = false;

  donsSharedCollection: IDon[] = [];

  editForm = this.fb.group({
    id: [],
    typeDonneur: [],
    prenom: [],
    nom: [null, [Validators.required]],
    email: [null, []],
    adresse: [],
    tel1: [null, []],
    ville: [],
    description: [],
    don: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected donneurService: DonneurService,
    protected donService: DonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donneur }) => {
      this.updateForm(donneur);

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
    const donneur = this.createFromForm();
    if (donneur.id !== undefined) {
      this.subscribeToSaveResponse(this.donneurService.update(donneur));
    } else {
      this.subscribeToSaveResponse(this.donneurService.create(donneur));
    }
  }

  trackDonById(index: number, item: IDon): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonneur>>): void {
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

  protected updateForm(donneur: IDonneur): void {
    this.editForm.patchValue({
      id: donneur.id,
      typeDonneur: donneur.typeDonneur,
      prenom: donneur.prenom,
      nom: donneur.nom,
      email: donneur.email,
      adresse: donneur.adresse,
      tel1: donneur.tel1,
      ville: donneur.ville,
      description: donneur.description,
      don: donneur.don,
    });

    this.donsSharedCollection = this.donService.addDonToCollectionIfMissing(this.donsSharedCollection, donneur.don);
  }

  protected loadRelationshipsOptions(): void {
    this.donService
      .query()
      .pipe(map((res: HttpResponse<IDon[]>) => res.body ?? []))
      .pipe(map((dons: IDon[]) => this.donService.addDonToCollectionIfMissing(dons, this.editForm.get('don')!.value)))
      .subscribe((dons: IDon[]) => (this.donsSharedCollection = dons));
  }

  protected createFromForm(): IDonneur {
    return {
      ...new Donneur(),
      id: this.editForm.get(['id'])!.value,
      typeDonneur: this.editForm.get(['typeDonneur'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      email: this.editForm.get(['email'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      tel1: this.editForm.get(['tel1'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      description: this.editForm.get(['description'])!.value,
      don: this.editForm.get(['don'])!.value,
    };
  }
}
