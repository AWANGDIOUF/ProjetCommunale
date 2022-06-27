import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResultatExamen, ResultatExamen } from '../resultat-examen.model';
import { ResultatExamenService } from '../service/resultat-examen.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { Examen } from 'app/entities/enumerations/examen.model';

@Component({
  selector: 'jhi-resultat-examen-update',
  templateUrl: './resultat-examen-update.component.html',
})
export class ResultatExamenUpdateComponent implements OnInit {
  isSaving = false;
  examenValues = Object.keys(Examen);

  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    typeExament: [],
    autreExamen: [],
    tauxReuissite: [],
    annee: [],
    etablissement: [],
  });

  constructor(
    protected resultatExamenService: ResultatExamenService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultatExamen }) => {
      this.updateForm(resultatExamen);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resultatExamen = this.createFromForm();
    if (resultatExamen.id !== undefined) {
      this.subscribeToSaveResponse(this.resultatExamenService.update(resultatExamen));
    } else {
      this.subscribeToSaveResponse(this.resultatExamenService.create(resultatExamen));
    }
  }

  trackEtablissementById(_index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResultatExamen>>): void {
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

  protected updateForm(resultatExamen: IResultatExamen): void {
    this.editForm.patchValue({
      id: resultatExamen.id,
      typeExament: resultatExamen.typeExament,
      autreExamen: resultatExamen.autreExamen,
      tauxReuissite: resultatExamen.tauxReuissite,
      annee: resultatExamen.annee,
      etablissement: resultatExamen.etablissement,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      resultatExamen.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IResultatExamen {
    return {
      ...new ResultatExamen(),
      id: this.editForm.get(['id'])!.value,
      typeExament: this.editForm.get(['typeExament'])!.value,
      autreExamen: this.editForm.get(['autreExamen'])!.value,
      tauxReuissite: this.editForm.get(['tauxReuissite'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
