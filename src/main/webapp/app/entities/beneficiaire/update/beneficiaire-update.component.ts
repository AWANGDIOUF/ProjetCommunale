import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { BeneficiaireService } from '../service/beneficiaire.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

@Component({
  selector: 'jhi-beneficiaire-update',
  templateUrl: './beneficiaire-update.component.html',
})
export class BeneficiaireUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];

  editForm = this.fb.group({
    id: [],
    typeBenefiaire: [],
    typePersoMoral: [],
    prenom: [],
    nom: [],
    cin: [null, []],
    adresse: [],
    tel1: [null, [Validators.required]],
    autretel1: [null, []],
    emailAssociation: [null, []],
    nomPresident: [],
    description: [],
    annonce: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected beneficiaireService: BeneficiaireService,
    protected annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiaire }) => {
      this.updateForm(beneficiaire);

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
    const beneficiaire = this.createFromForm();
    if (beneficiaire.id !== undefined) {
      this.subscribeToSaveResponse(this.beneficiaireService.update(beneficiaire));
    } else {
      this.subscribeToSaveResponse(this.beneficiaireService.create(beneficiaire));
    }
  }

  trackAnnonceById(index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeneficiaire>>): void {
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

  protected updateForm(beneficiaire: IBeneficiaire): void {
    this.editForm.patchValue({
      id: beneficiaire.id,
      typeBenefiaire: beneficiaire.typeBenefiaire,
      typePersoMoral: beneficiaire.typePersoMoral,
      prenom: beneficiaire.prenom,
      nom: beneficiaire.nom,
      cin: beneficiaire.cin,
      adresse: beneficiaire.adresse,
      tel1: beneficiaire.tel1,
      autretel1: beneficiaire.autretel1,
      emailAssociation: beneficiaire.emailAssociation,
      nomPresident: beneficiaire.nomPresident,
      description: beneficiaire.description,
      annonce: beneficiaire.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(
      this.annoncesSharedCollection,
      beneficiaire.annonce
    );
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

  protected createFromForm(): IBeneficiaire {
    return {
      ...new Beneficiaire(),
      id: this.editForm.get(['id'])!.value,
      typeBenefiaire: this.editForm.get(['typeBenefiaire'])!.value,
      typePersoMoral: this.editForm.get(['typePersoMoral'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      cin: this.editForm.get(['cin'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      tel1: this.editForm.get(['tel1'])!.value,
      autretel1: this.editForm.get(['autretel1'])!.value,
      emailAssociation: this.editForm.get(['emailAssociation'])!.value,
      nomPresident: this.editForm.get(['nomPresident'])!.value,
      description: this.editForm.get(['description'])!.value,
      annonce: this.editForm.get(['annonce'])!.value,
    };
  }
}
