import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVainqueur, Vainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';
import { ICombattant } from 'app/entities/combattant/combattant.model';
import { CombattantService } from 'app/entities/combattant/service/combattant.service';

@Component({
  selector: 'jhi-vainqueur-update',
  templateUrl: './vainqueur-update.component.html',
})
export class VainqueurUpdateComponent implements OnInit {
  isSaving = false;

  competitionsSharedCollection: ICompetition[] = [];
  combattantsSharedCollection: ICombattant[] = [];

  editForm = this.fb.group({
    id: [],
    prix: [],
    competition: [],
    combattant: [],
  });

  constructor(
    protected vainqueurService: VainqueurService,
    protected competitionService: CompetitionService,
    protected combattantService: CombattantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vainqueur }) => {
      this.updateForm(vainqueur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vainqueur = this.createFromForm();
    if (vainqueur.id !== undefined) {
      this.subscribeToSaveResponse(this.vainqueurService.update(vainqueur));
    } else {
      this.subscribeToSaveResponse(this.vainqueurService.create(vainqueur));
    }
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  trackCombattantById(_index: number, item: ICombattant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVainqueur>>): void {
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

  protected updateForm(vainqueur: IVainqueur): void {
    this.editForm.patchValue({
      id: vainqueur.id,
      prix: vainqueur.prix,
      competition: vainqueur.competition,
      combattant: vainqueur.combattant,
    });

    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      vainqueur.competition
    );
    this.combattantsSharedCollection = this.combattantService.addCombattantToCollectionIfMissing(
      this.combattantsSharedCollection,
      vainqueur.combattant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing(competitions, this.editForm.get('competition')!.value)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));

    this.combattantService
      .query()
      .pipe(map((res: HttpResponse<ICombattant[]>) => res.body ?? []))
      .pipe(
        map((combattants: ICombattant[]) =>
          this.combattantService.addCombattantToCollectionIfMissing(combattants, this.editForm.get('combattant')!.value)
        )
      )
      .subscribe((combattants: ICombattant[]) => (this.combattantsSharedCollection = combattants));
  }

  protected createFromForm(): IVainqueur {
    return {
      ...new Vainqueur(),
      id: this.editForm.get(['id'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      competition: this.editForm.get(['competition'])!.value,
      combattant: this.editForm.get(['combattant'])!.value,
    };
  }
}
