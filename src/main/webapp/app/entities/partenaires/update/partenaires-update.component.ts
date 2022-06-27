import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPartenaires, Partenaires } from '../partenaires.model';
import { PartenairesService } from '../service/partenaires.service';
import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';
import { EntrepreneurService } from 'app/entities/entrepreneur/service/entrepreneur.service';
import { IEleveur } from 'app/entities/eleveur/eleveur.model';
import { EleveurService } from 'app/entities/eleveur/service/eleveur.service';

@Component({
  selector: 'jhi-partenaires-update',
  templateUrl: './partenaires-update.component.html',
})
export class PartenairesUpdateComponent implements OnInit {
  isSaving = false;

  entrepreneursCollection: IEntrepreneur[] = [];
  eleveursCollection: IEleveur[] = [];

  editForm = this.fb.group({
    id: [],
    nomPartenaire: [],
    emailPartenaire: [],
    telPartenaire: [],
    description: [],
    entrepreneur: [],
    eleveur: [],
  });

  constructor(
    protected partenairesService: PartenairesService,
    protected entrepreneurService: EntrepreneurService,
    protected eleveurService: EleveurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partenaires }) => {
      this.updateForm(partenaires);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partenaires = this.createFromForm();
    if (partenaires.id !== undefined) {
      this.subscribeToSaveResponse(this.partenairesService.update(partenaires));
    } else {
      this.subscribeToSaveResponse(this.partenairesService.create(partenaires));
    }
  }

  trackEntrepreneurById(_index: number, item: IEntrepreneur): number {
    return item.id!;
  }

  trackEleveurById(_index: number, item: IEleveur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartenaires>>): void {
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

  protected updateForm(partenaires: IPartenaires): void {
    this.editForm.patchValue({
      id: partenaires.id,
      nomPartenaire: partenaires.nomPartenaire,
      emailPartenaire: partenaires.emailPartenaire,
      telPartenaire: partenaires.telPartenaire,
      description: partenaires.description,
      entrepreneur: partenaires.entrepreneur,
      eleveur: partenaires.eleveur,
    });

    this.entrepreneursCollection = this.entrepreneurService.addEntrepreneurToCollectionIfMissing(
      this.entrepreneursCollection,
      partenaires.entrepreneur
    );
    this.eleveursCollection = this.eleveurService.addEleveurToCollectionIfMissing(this.eleveursCollection, partenaires.eleveur);
  }

  protected loadRelationshipsOptions(): void {
    this.entrepreneurService
      .query({ filter: 'partenaires-is-null' })
      .pipe(map((res: HttpResponse<IEntrepreneur[]>) => res.body ?? []))
      .pipe(
        map((entrepreneurs: IEntrepreneur[]) =>
          this.entrepreneurService.addEntrepreneurToCollectionIfMissing(entrepreneurs, this.editForm.get('entrepreneur')!.value)
        )
      )
      .subscribe((entrepreneurs: IEntrepreneur[]) => (this.entrepreneursCollection = entrepreneurs));

    this.eleveurService
      .query({ filter: 'partenaires-is-null' })
      .pipe(map((res: HttpResponse<IEleveur[]>) => res.body ?? []))
      .pipe(
        map((eleveurs: IEleveur[]) => this.eleveurService.addEleveurToCollectionIfMissing(eleveurs, this.editForm.get('eleveur')!.value))
      )
      .subscribe((eleveurs: IEleveur[]) => (this.eleveursCollection = eleveurs));
  }

  protected createFromForm(): IPartenaires {
    return {
      ...new Partenaires(),
      id: this.editForm.get(['id'])!.value,
      nomPartenaire: this.editForm.get(['nomPartenaire'])!.value,
      emailPartenaire: this.editForm.get(['emailPartenaire'])!.value,
      telPartenaire: this.editForm.get(['telPartenaire'])!.value,
      description: this.editForm.get(['description'])!.value,
      entrepreneur: this.editForm.get(['entrepreneur'])!.value,
      eleveur: this.editForm.get(['eleveur'])!.value,
    };
  }
}
