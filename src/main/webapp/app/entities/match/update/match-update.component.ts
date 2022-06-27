import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMatch, Match } from '../match.model';
import { MatchService } from '../service/match.service';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html',
})
export class MatchUpdateComponent implements OnInit {
  isSaving = false;

  equipesSharedCollection: IEquipe[] = [];

  editForm = this.fb.group({
    id: [],
    dateMatch: [],
    lieuMatch: [],
    scoreMatch: [],
    equipe: [],
  });

  constructor(
    protected matchService: MatchService,
    protected equipeService: EquipeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ match }) => {
      if (match.id === undefined) {
        const today = dayjs().startOf('day');
        match.dateMatch = today;
      }

      this.updateForm(match);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  trackEquipeById(_index: number, item: IEquipe): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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

  protected updateForm(match: IMatch): void {
    this.editForm.patchValue({
      id: match.id,
      dateMatch: match.dateMatch ? match.dateMatch.format(DATE_TIME_FORMAT) : null,
      lieuMatch: match.lieuMatch,
      scoreMatch: match.scoreMatch,
      equipe: match.equipe,
    });

    this.equipesSharedCollection = this.equipeService.addEquipeToCollectionIfMissing(this.equipesSharedCollection, match.equipe);
  }

  protected loadRelationshipsOptions(): void {
    this.equipeService
      .query()
      .pipe(map((res: HttpResponse<IEquipe[]>) => res.body ?? []))
      .pipe(map((equipes: IEquipe[]) => this.equipeService.addEquipeToCollectionIfMissing(equipes, this.editForm.get('equipe')!.value)))
      .subscribe((equipes: IEquipe[]) => (this.equipesSharedCollection = equipes));
  }

  protected createFromForm(): IMatch {
    return {
      ...new Match(),
      id: this.editForm.get(['id'])!.value,
      dateMatch: this.editForm.get(['dateMatch'])!.value ? dayjs(this.editForm.get(['dateMatch'])!.value, DATE_TIME_FORMAT) : undefined,
      lieuMatch: this.editForm.get(['lieuMatch'])!.value,
      scoreMatch: this.editForm.get(['scoreMatch'])!.value,
      equipe: this.editForm.get(['equipe'])!.value,
    };
  }
}
