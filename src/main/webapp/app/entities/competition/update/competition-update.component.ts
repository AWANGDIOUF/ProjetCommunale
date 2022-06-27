import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompetition, Competition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';
import { DisciplineClub } from 'app/entities/enumerations/discipline-club.model';

@Component({
  selector: 'jhi-competition-update',
  templateUrl: './competition-update.component.html',
})
export class CompetitionUpdateComponent implements OnInit {
  isSaving = false;
  disciplineClubValues = Object.keys(DisciplineClub);

  clubsSharedCollection: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    dateCompetition: [],
    lieuCompetition: [],
    discipline: [],
    club: [],
  });

  constructor(
    protected competitionService: CompetitionService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competition }) => {
      if (competition.id === undefined) {
        const today = dayjs().startOf('day');
        competition.dateCompetition = today;
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

  trackClubById(_index: number, item: IClub): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetition>>): void {
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

  protected updateForm(competition: ICompetition): void {
    this.editForm.patchValue({
      id: competition.id,
      dateCompetition: competition.dateCompetition ? competition.dateCompetition.format(DATE_TIME_FORMAT) : null,
      lieuCompetition: competition.lieuCompetition,
      discipline: competition.discipline,
      club: competition.club,
    });

    this.clubsSharedCollection = this.clubService.addClubToCollectionIfMissing(this.clubsSharedCollection, competition.club);
  }

  protected loadRelationshipsOptions(): void {
    this.clubService
      .query()
      .pipe(map((res: HttpResponse<IClub[]>) => res.body ?? []))
      .pipe(map((clubs: IClub[]) => this.clubService.addClubToCollectionIfMissing(clubs, this.editForm.get('club')!.value)))
      .subscribe((clubs: IClub[]) => (this.clubsSharedCollection = clubs));
  }

  protected createFromForm(): ICompetition {
    return {
      ...new Competition(),
      id: this.editForm.get(['id'])!.value,
      dateCompetition: this.editForm.get(['dateCompetition'])!.value
        ? dayjs(this.editForm.get(['dateCompetition'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lieuCompetition: this.editForm.get(['lieuCompetition'])!.value,
      discipline: this.editForm.get(['discipline'])!.value,
      club: this.editForm.get(['club'])!.value,
    };
  }
}
