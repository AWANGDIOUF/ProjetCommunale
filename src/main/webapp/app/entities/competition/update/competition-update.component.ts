import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompetition, Competition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { VainqueurService } from 'app/entities/vainqueur/service/vainqueur.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

@Component({
  selector: 'jhi-competition-update',
  templateUrl: './competition-update.component.html',
})
export class CompetitionUpdateComponent implements OnInit {
  isSaving = false;

  vainqueursSharedCollection: IVainqueur[] = [];
  clubsSharedCollection: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    lieu: [],
    discipline: [],
    vainqueur: [],
    clubs: [],
  });

  constructor(
    protected competitionService: CompetitionService,
    protected vainqueurService: VainqueurService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competition }) => {
      if (competition.id === undefined) {
        const today = dayjs().startOf('day');
        competition.date = today;
      }

      this.updateForm(competition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competition = this.createFromForm();
    if (competition.id !== undefined) {
      this.subscribeToSaveResponse(this.competitionService.update(competition));
    } else {
      this.subscribeToSaveResponse(this.competitionService.create(competition));
    }
  }

  trackVainqueurById(index: number, item: IVainqueur): number {
    return item.id!;
  }

  trackClubById(index: number, item: IClub): number {
    return item.id!;
  }

  getSelectedClub(option: IClub, selectedVals?: IClub[]): IClub {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetition>>): void {
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

  protected updateForm(competition: ICompetition): void {
    this.editForm.patchValue({
      id: competition.id,
      date: competition.date ? competition.date.format(DATE_TIME_FORMAT) : null,
      lieu: competition.lieu,
      discipline: competition.discipline,
      vainqueur: competition.vainqueur,
      clubs: competition.clubs,
    });

    this.vainqueursSharedCollection = this.vainqueurService.addVainqueurToCollectionIfMissing(
      this.vainqueursSharedCollection,
      competition.vainqueur
    );
    this.clubsSharedCollection = this.clubService.addClubToCollectionIfMissing(this.clubsSharedCollection, ...(competition.clubs ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.vainqueurService
      .query()
      .pipe(map((res: HttpResponse<IVainqueur[]>) => res.body ?? []))
      .pipe(
        map((vainqueurs: IVainqueur[]) =>
          this.vainqueurService.addVainqueurToCollectionIfMissing(vainqueurs, this.editForm.get('vainqueur')!.value)
        )
      )
      .subscribe((vainqueurs: IVainqueur[]) => (this.vainqueursSharedCollection = vainqueurs));

    this.clubService
      .query()
      .pipe(map((res: HttpResponse<IClub[]>) => res.body ?? []))
      .pipe(map((clubs: IClub[]) => this.clubService.addClubToCollectionIfMissing(clubs, ...(this.editForm.get('clubs')!.value ?? []))))
      .subscribe((clubs: IClub[]) => (this.clubsSharedCollection = clubs));
  }

  protected createFromForm(): ICompetition {
    return {
      ...new Competition(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      lieu: this.editForm.get(['lieu'])!.value,
      discipline: this.editForm.get(['discipline'])!.value,
      vainqueur: this.editForm.get(['vainqueur'])!.value,
      clubs: this.editForm.get(['clubs'])!.value,
    };
  }
}
