import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArchiveSport, ArchiveSport } from '../archive-sport.model';
import { ArchiveSportService } from '../service/archive-sport.service';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

@Component({
  selector: 'jhi-archive-sport-update',
  templateUrl: './archive-sport-update.component.html',
})
export class ArchiveSportUpdateComponent implements OnInit {
  isSaving = false;

  equipesSharedCollection: IEquipe[] = [];
  clubsSharedCollection: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    annee: [],
    equipe: [],
    club: [],
  });

  constructor(
    protected archiveSportService: ArchiveSportService,
    protected equipeService: EquipeService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ archiveSport }) => {
      this.updateForm(archiveSport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const archiveSport = this.createFromForm();
    if (archiveSport.id !== undefined) {
      this.subscribeToSaveResponse(this.archiveSportService.update(archiveSport));
    } else {
      this.subscribeToSaveResponse(this.archiveSportService.create(archiveSport));
    }
  }

  trackEquipeById(_index: number, item: IEquipe): number {
    return item.id!;
  }

  trackClubById(_index: number, item: IClub): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArchiveSport>>): void {
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

  protected updateForm(archiveSport: IArchiveSport): void {
    this.editForm.patchValue({
      id: archiveSport.id,
      annee: archiveSport.annee,
      equipe: archiveSport.equipe,
      club: archiveSport.club,
    });

    this.equipesSharedCollection = this.equipeService.addEquipeToCollectionIfMissing(this.equipesSharedCollection, archiveSport.equipe);
    this.clubsSharedCollection = this.clubService.addClubToCollectionIfMissing(this.clubsSharedCollection, archiveSport.club);
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
  }

  protected createFromForm(): IArchiveSport {
    return {
      ...new ArchiveSport(),
      id: this.editForm.get(['id'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      equipe: this.editForm.get(['equipe'])!.value,
      club: this.editForm.get(['club'])!.value,
    };
  }
}
