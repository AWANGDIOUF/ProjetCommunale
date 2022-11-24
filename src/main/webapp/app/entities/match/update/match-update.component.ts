import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
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
    date: [],
    lieu: [],
    score: [],
    equipes: [],
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
        match.date = today;
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

  trackEquipeById(index: number, item: IEquipe): number {
    return item.id!;
  }

  getSelectedEquipe(option: IEquipe, selectedVals?: IEquipe[]): IEquipe {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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

  protected updateForm(match: IMatch): void {
    this.editForm.patchValue({
      id: match.id,
      date: match.date ? match.date.format(DATE_TIME_FORMAT) : null,
      lieu: match.lieu,
      score: match.score,
      equipes: match.equipes,
    });

    this.equipesSharedCollection = this.equipeService.addEquipeToCollectionIfMissing(
      this.equipesSharedCollection,
      ...(match.equipes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equipeService
      .query()
      .pipe(map((res: HttpResponse<IEquipe[]>) => res.body ?? []))
      .pipe(
        map((equipes: IEquipe[]) =>
          this.equipeService.addEquipeToCollectionIfMissing(equipes, ...(this.editForm.get('equipes')!.value ?? []))
        )
      )
      .subscribe((equipes: IEquipe[]) => (this.equipesSharedCollection = equipes));
  }

  protected createFromForm(): IMatch {
    return {
      ...new Match(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      lieu: this.editForm.get(['lieu'])!.value,
      score: this.editForm.get(['score'])!.value,
      equipes: this.editForm.get(['equipes'])!.value,
    };
  }
}
