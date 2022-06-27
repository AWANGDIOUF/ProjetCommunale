import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEleveur, Eleveur } from '../eleveur.model';
import { EleveurService } from '../service/eleveur.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { TypeElevage } from 'app/entities/enumerations/type-elevage.model';

@Component({
  selector: 'jhi-eleveur-update',
  templateUrl: './eleveur-update.component.html',
})
export class EleveurUpdateComponent implements OnInit {
  isSaving = false;
  typeElevageValues = Object.keys(TypeElevage);

  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomEleveur: [],
    prenomEleveur: [],
    telEleveur: [],
    tel1Eleveur: [],
    adresse: [],
    nomElevage: [],
    descriptionActivite: [],
    quartier: [],
  });

  constructor(
    protected eleveurService: EleveurService,
    protected quartierService: QuartierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eleveur }) => {
      this.updateForm(eleveur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eleveur = this.createFromForm();
    if (eleveur.id !== undefined) {
      this.subscribeToSaveResponse(this.eleveurService.update(eleveur));
    } else {
      this.subscribeToSaveResponse(this.eleveurService.create(eleveur));
    }
  }

  trackQuartierById(_index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEleveur>>): void {
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

  protected updateForm(eleveur: IEleveur): void {
    this.editForm.patchValue({
      id: eleveur.id,
      nomEleveur: eleveur.nomEleveur,
      prenomEleveur: eleveur.prenomEleveur,
      telEleveur: eleveur.telEleveur,
      tel1Eleveur: eleveur.tel1Eleveur,
      adresse: eleveur.adresse,
      nomElevage: eleveur.nomElevage,
      descriptionActivite: eleveur.descriptionActivite,
      quartier: eleveur.quartier,
    });

    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(
      this.quartiersSharedCollection,
      eleveur.quartier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('quartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));
  }

  protected createFromForm(): IEleveur {
    return {
      ...new Eleveur(),
      id: this.editForm.get(['id'])!.value,
      nomEleveur: this.editForm.get(['nomEleveur'])!.value,
      prenomEleveur: this.editForm.get(['prenomEleveur'])!.value,
      telEleveur: this.editForm.get(['telEleveur'])!.value,
      tel1Eleveur: this.editForm.get(['tel1Eleveur'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      nomElevage: this.editForm.get(['nomElevage'])!.value,
      descriptionActivite: this.editForm.get(['descriptionActivite'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
    };
  }
}
