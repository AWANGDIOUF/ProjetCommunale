import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemandeInterview, DemandeInterview } from '../demande-interview.model';
import { DemandeInterviewService } from '../service/demande-interview.service';
import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';
import { EntrepreneurService } from 'app/entities/entrepreneur/service/entrepreneur.service';

@Component({
  selector: 'jhi-demande-interview-update',
  templateUrl: './demande-interview-update.component.html',
})
export class DemandeInterviewUpdateComponent implements OnInit {
  isSaving = false;

  entrepreneursCollection: IEntrepreneur[] = [];

  editForm = this.fb.group({
    id: [],
    nomJournaliste: [],
    prenomJournaliste: [],
    nomSociete: [null, [Validators.required]],
    emailJournalite: [null, [Validators.required]],
    dateInterview: [],
    etatDemande: [],
    entrepreneur: [],
  });

  constructor(
    protected demandeInterviewService: DemandeInterviewService,
    protected entrepreneurService: EntrepreneurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInterview }) => {
      this.updateForm(demandeInterview);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeInterview = this.createFromForm();
    if (demandeInterview.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeInterviewService.update(demandeInterview));
    } else {
      this.subscribeToSaveResponse(this.demandeInterviewService.create(demandeInterview));
    }
  }

  trackEntrepreneurById(_index: number, item: IEntrepreneur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeInterview>>): void {
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

  protected updateForm(demandeInterview: IDemandeInterview): void {
    this.editForm.patchValue({
      id: demandeInterview.id,
      nomJournaliste: demandeInterview.nomJournaliste,
      prenomJournaliste: demandeInterview.prenomJournaliste,
      nomSociete: demandeInterview.nomSociete,
      emailJournalite: demandeInterview.emailJournalite,
      dateInterview: demandeInterview.dateInterview,
      etatDemande: demandeInterview.etatDemande,
      entrepreneur: demandeInterview.entrepreneur,
    });

    this.entrepreneursCollection = this.entrepreneurService.addEntrepreneurToCollectionIfMissing(
      this.entrepreneursCollection,
      demandeInterview.entrepreneur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.entrepreneurService
      .query({ filter: 'demandeinterview-is-null' })
      .pipe(map((res: HttpResponse<IEntrepreneur[]>) => res.body ?? []))
      .pipe(
        map((entrepreneurs: IEntrepreneur[]) =>
          this.entrepreneurService.addEntrepreneurToCollectionIfMissing(entrepreneurs, this.editForm.get('entrepreneur')!.value)
        )
      )
      .subscribe((entrepreneurs: IEntrepreneur[]) => (this.entrepreneursCollection = entrepreneurs));
  }

  protected createFromForm(): IDemandeInterview {
    return {
      ...new DemandeInterview(),
      id: this.editForm.get(['id'])!.value,
      nomJournaliste: this.editForm.get(['nomJournaliste'])!.value,
      prenomJournaliste: this.editForm.get(['prenomJournaliste'])!.value,
      nomSociete: this.editForm.get(['nomSociete'])!.value,
      emailJournalite: this.editForm.get(['emailJournalite'])!.value,
      dateInterview: this.editForm.get(['dateInterview'])!.value,
      etatDemande: this.editForm.get(['etatDemande'])!.value,
      entrepreneur: this.editForm.get(['entrepreneur'])!.value,
    };
  }
}
