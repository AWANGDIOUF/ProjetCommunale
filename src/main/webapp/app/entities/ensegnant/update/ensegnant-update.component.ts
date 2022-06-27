import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEnsegnant, Ensegnant } from '../ensegnant.model';
import { EnsegnantService } from '../service/ensegnant.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

@Component({
  selector: 'jhi-ensegnant-update',
  templateUrl: './ensegnant-update.component.html',
})
export class EnsegnantUpdateComponent implements OnInit {
  isSaving = false;

  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    email: [null, [Validators.required]],
    tel: [null, [Validators.required]],
    tel1: [null, []],
    etablissement: [],
  });

  constructor(
    protected ensegnantService: EnsegnantService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ensegnant }) => {
      this.updateForm(ensegnant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ensegnant = this.createFromForm();
    if (ensegnant.id !== undefined) {
      this.subscribeToSaveResponse(this.ensegnantService.update(ensegnant));
    } else {
      this.subscribeToSaveResponse(this.ensegnantService.create(ensegnant));
    }
  }

  trackEtablissementById(_index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnsegnant>>): void {
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

  protected updateForm(ensegnant: IEnsegnant): void {
    this.editForm.patchValue({
      id: ensegnant.id,
      nom: ensegnant.nom,
      prenom: ensegnant.prenom,
      email: ensegnant.email,
      tel: ensegnant.tel,
      tel1: ensegnant.tel1,
      etablissement: ensegnant.etablissement,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      ensegnant.etablissement
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

  protected createFromForm(): IEnsegnant {
    return {
      ...new Ensegnant(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      tel1: this.editForm.get(['tel1'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
