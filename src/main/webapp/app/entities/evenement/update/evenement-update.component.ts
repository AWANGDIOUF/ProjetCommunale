import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvenement, Evenement } from '../evenement.model';
import { EvenementService } from '../service/evenement.service';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

@Component({
  selector: 'jhi-evenement-update',
  templateUrl: './evenement-update.component.html',
})
export class EvenementUpdateComponent implements OnInit {
  isSaving = false;

  artistesCollection: IArtiste[] = [];

  editForm = this.fb.group({
    id: [],
    nomEvenement: [],
    libelle: [],
    action: [],
    decision: [],
    delaiInstruction: [],
    delaiInscription: [],
    delaiValidation: [],
    artiste: [],
  });

  constructor(
    protected evenementService: EvenementService,
    protected artisteService: ArtisteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => {
      if (evenement.id === undefined) {
        const today = dayjs().startOf('day');
        evenement.delaiInstruction = today;
        evenement.delaiInscription = today;
        evenement.delaiValidation = today;
      }

      this.updateForm(evenement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evenement = this.createFromForm();
    if (evenement.id !== undefined) {
      this.subscribeToSaveResponse(this.evenementService.update(evenement));
    } else {
      this.subscribeToSaveResponse(this.evenementService.create(evenement));
    }
  }

  trackArtisteById(_index: number, item: IArtiste): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvenement>>): void {
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

  protected updateForm(evenement: IEvenement): void {
    this.editForm.patchValue({
      id: evenement.id,
      nomEvenement: evenement.nomEvenement,
      libelle: evenement.libelle,
      action: evenement.action,
      decision: evenement.decision,
      delaiInstruction: evenement.delaiInstruction ? evenement.delaiInstruction.format(DATE_TIME_FORMAT) : null,
      delaiInscription: evenement.delaiInscription ? evenement.delaiInscription.format(DATE_TIME_FORMAT) : null,
      delaiValidation: evenement.delaiValidation ? evenement.delaiValidation.format(DATE_TIME_FORMAT) : null,
      artiste: evenement.artiste,
    });

    this.artistesCollection = this.artisteService.addArtisteToCollectionIfMissing(this.artistesCollection, evenement.artiste);
  }

  protected loadRelationshipsOptions(): void {
    this.artisteService
      .query({ filter: 'evenement-is-null' })
      .pipe(map((res: HttpResponse<IArtiste[]>) => res.body ?? []))
      .pipe(
        map((artistes: IArtiste[]) => this.artisteService.addArtisteToCollectionIfMissing(artistes, this.editForm.get('artiste')!.value))
      )
      .subscribe((artistes: IArtiste[]) => (this.artistesCollection = artistes));
  }

  protected createFromForm(): IEvenement {
    return {
      ...new Evenement(),
      id: this.editForm.get(['id'])!.value,
      nomEvenement: this.editForm.get(['nomEvenement'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      action: this.editForm.get(['action'])!.value,
      decision: this.editForm.get(['decision'])!.value,
      delaiInstruction: this.editForm.get(['delaiInstruction'])!.value
        ? dayjs(this.editForm.get(['delaiInstruction'])!.value, DATE_TIME_FORMAT)
        : undefined,
      delaiInscription: this.editForm.get(['delaiInscription'])!.value
        ? dayjs(this.editForm.get(['delaiInscription'])!.value, DATE_TIME_FORMAT)
        : undefined,
      delaiValidation: this.editForm.get(['delaiValidation'])!.value
        ? dayjs(this.editForm.get(['delaiValidation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      artiste: this.editForm.get(['artiste'])!.value,
    };
  }
}
