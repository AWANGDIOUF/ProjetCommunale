import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuartier, Quartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { BeneficiaireService } from 'app/entities/beneficiaire/service/beneficiaire.service';

@Component({
  selector: 'jhi-quartier-update',
  templateUrl: './quartier-update.component.html',
})
export class QuartierUpdateComponent implements OnInit {
  isSaving = false;

  equipesSharedCollection: IEquipe[] = [];
  clubsSharedCollection: IClub[] = [];
  beneficiairesSharedCollection: IBeneficiaire[] = [];

  editForm = this.fb.group({
    id: [],
    nomQuartier: [],
    equipe: [],
    club: [],
    beneficiaire: [],
  });

  constructor(
    protected quartierService: QuartierService,
    protected equipeService: EquipeService,
    protected clubService: ClubService,
    protected beneficiaireService: BeneficiaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quartier }) => {
      this.updateForm(quartier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quartier = this.createFromForm();
    if (quartier.id !== undefined) {
      this.subscribeToSaveResponse(this.quartierService.update(quartier));
    } else {
      this.subscribeToSaveResponse(this.quartierService.create(quartier));
    }
  }

  trackEquipeById(index: number, item: IEquipe): number {
    return item.id!;
  }

  trackClubById(index: number, item: IClub): number {
    return item.id!;
  }

  trackBeneficiaireById(index: number, item: IBeneficiaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuartier>>): void {
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

  protected updateForm(quartier: IQuartier): void {
    this.editForm.patchValue({
      id: quartier.id,
      nomQuartier: quartier.nomQuartier,
      equipe: quartier.equipe,
      club: quartier.club,
      beneficiaire: quartier.beneficiaire,
    });

    this.equipesSharedCollection = this.equipeService.addEquipeToCollectionIfMissing(this.equipesSharedCollection, quartier.equipe);
    this.clubsSharedCollection = this.clubService.addClubToCollectionIfMissing(this.clubsSharedCollection, quartier.club);
    this.beneficiairesSharedCollection = this.beneficiaireService.addBeneficiaireToCollectionIfMissing(
      this.beneficiairesSharedCollection,
      quartier.beneficiaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equipeService
      .query()
      .pipe(map((res: HttpResponse<IEquipe[]>) => res.body ?? []))
      .pipe(map((equipes: IEquipe[]) => this.equipeService.addEquipeToCollectionIfMissing(equipes, this.editForm.get('equipe')!.value)))
      .subscribe((equipes: IEquipe[]) => (this.equipesSharedCollection = equipes));

    this.clubService
      .query()
      .pipe(map((res: HttpResponse<IClub[]>) => res.body ?? []))
      .pipe(map((clubs: IClub[]) => this.clubService.addClubToCollectionIfMissing(clubs, this.editForm.get('club')!.value)))
      .subscribe((clubs: IClub[]) => (this.clubsSharedCollection = clubs));

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

  protected createFromForm(): IQuartier {
    return {
      ...new Quartier(),
      id: this.editForm.get(['id'])!.value,
      nomQuartier: this.editForm.get(['nomQuartier'])!.value,
      equipe: this.editForm.get(['equipe'])!.value,
      club: this.editForm.get(['club'])!.value,
      beneficiaire: this.editForm.get(['beneficiaire'])!.value,
    };
  }
}
