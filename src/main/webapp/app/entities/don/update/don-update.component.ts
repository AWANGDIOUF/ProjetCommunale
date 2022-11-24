import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDon, Don } from '../don.model';
import { DonService } from '../service/don.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

@Component({
  selector: 'jhi-don-update',
  templateUrl: './don-update.component.html',
})
export class DonUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];

  editForm = this.fb.group({
    id: [],
    typeDon: [],
    montant: [],
    description: [],
    annonce: [],
  });

  constructor(
    protected donService: DonService,
    protected annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ don }) => {
      this.updateForm(don);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const don = this.createFromForm();
    if (don.id !== undefined) {
      this.subscribeToSaveResponse(this.donService.update(don));
    } else {
      this.subscribeToSaveResponse(this.donService.create(don));
    }
  }

  trackAnnonceById(index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDon>>): void {
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

  protected updateForm(don: IDon): void {
    this.editForm.patchValue({
      id: don.id,
      typeDon: don.typeDon,
      montant: don.montant,
      description: don.description,
      annonce: don.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, don.annonce);
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

  protected createFromForm(): IDon {
    return {
      ...new Don(),
      id: this.editForm.get(['id'])!.value,
      typeDon: this.editForm.get(['typeDon'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      description: this.editForm.get(['description'])!.value,
      annonce: this.editForm.get(['annonce'])!.value,
    };
  }
}
