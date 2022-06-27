import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnnonce, Annonce } from '../annonce.model';
import { AnnonceService } from '../service/annonce.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDon } from 'app/entities/don/don.model';
import { DonService } from 'app/entities/don/service/don.service';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { BeneficiaireService } from 'app/entities/beneficiaire/service/beneficiaire.service';

@Component({
  selector: 'jhi-annonce-update',
  templateUrl: './annonce-update.component.html',
})
export class AnnonceUpdateComponent implements OnInit {
  isSaving = false;

  donsSharedCollection: IDon[] = [];
  beneficiairesSharedCollection: IBeneficiaire[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [],
    description: [],
    date: [],
    lieu: [],
    don: [],
    beneficiaire: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected annonceService: AnnonceService,
    protected donService: DonService,
    protected beneficiaireService: BeneficiaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.updateForm(annonce);

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
    const annonce = this.createFromForm();
    if (annonce.id !== undefined) {
      this.subscribeToSaveResponse(this.annonceService.update(annonce));
    } else {
      this.subscribeToSaveResponse(this.annonceService.create(annonce));
    }
  }

  trackDonById(_index: number, item: IDon): number {
    return item.id!;
  }

  trackBeneficiaireById(_index: number, item: IBeneficiaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnonce>>): void {
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

  protected updateForm(annonce: IAnnonce): void {
    this.editForm.patchValue({
      id: annonce.id,
      titre: annonce.titre,
      description: annonce.description,
      date: annonce.date,
      lieu: annonce.lieu,
      don: annonce.don,
      beneficiaire: annonce.beneficiaire,
    });

    this.donsSharedCollection = this.donService.addDonToCollectionIfMissing(this.donsSharedCollection, annonce.don);
    this.beneficiairesSharedCollection = this.beneficiaireService.addBeneficiaireToCollectionIfMissing(
      this.beneficiairesSharedCollection,
      annonce.beneficiaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.donService
      .query()
      .pipe(map((res: HttpResponse<IDon[]>) => res.body ?? []))
      .pipe(map((dons: IDon[]) => this.donService.addDonToCollectionIfMissing(dons, this.editForm.get('don')!.value)))
      .subscribe((dons: IDon[]) => (this.donsSharedCollection = dons));

    this.beneficiaireService
      .query()
      .pipe(map((res: HttpResponse<IBeneficiaire[]>) => res.body ?? []))
      .pipe(
        map((beneficiaires: IBeneficiaire[]) =>
          this.beneficiaireService.addBeneficiaireToCollectionIfMissing(beneficiaires, this.editForm.get('beneficiaire')!.value)
        )
      )
      .subscribe((beneficiaires: IBeneficiaire[]) => (this.beneficiairesSharedCollection = beneficiaires));
  }

  protected createFromForm(): IAnnonce {
    return {
      ...new Annonce(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      description: this.editForm.get(['description'])!.value,
      date: this.editForm.get(['date'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
      don: this.editForm.get(['don'])!.value,
      beneficiaire: this.editForm.get(['beneficiaire'])!.value,
    };
  }
}
