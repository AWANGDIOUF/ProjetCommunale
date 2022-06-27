import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInscription, Inscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { IEvenement } from 'app/entities/evenement/evenement.model';
import { EvenementService } from 'app/entities/evenement/service/evenement.service';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;

  evenementsCollection: IEvenement[] = [];

  editForm = this.fb.group({
    id: [],
    nomPers: [],
    prenomPers: [],
    emailPers: [null, []],
    telPers: [null, [Validators.required]],
    tel1Pers: [null, []],
    etatInscription: [],
    evenement: [],
  });

  constructor(
    protected inscriptionService: InscriptionService,
    protected evenementService: EvenementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.updateForm(inscription);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.createFromForm();
    if (inscription.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  trackEvenementById(_index: number, item: IEvenement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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

  protected updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      nomPers: inscription.nomPers,
      prenomPers: inscription.prenomPers,
      emailPers: inscription.emailPers,
      telPers: inscription.telPers,
      tel1Pers: inscription.tel1Pers,
      etatInscription: inscription.etatInscription,
      evenement: inscription.evenement,
    });

    this.evenementsCollection = this.evenementService.addEvenementToCollectionIfMissing(this.evenementsCollection, inscription.evenement);
  }

  protected loadRelationshipsOptions(): void {
    this.evenementService
      .query({ filter: 'inscription-is-null' })
      .pipe(map((res: HttpResponse<IEvenement[]>) => res.body ?? []))
      .pipe(
        map((evenements: IEvenement[]) =>
          this.evenementService.addEvenementToCollectionIfMissing(evenements, this.editForm.get('evenement')!.value)
        )
      )
      .subscribe((evenements: IEvenement[]) => (this.evenementsCollection = evenements));
  }

  protected createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      nomPers: this.editForm.get(['nomPers'])!.value,
      prenomPers: this.editForm.get(['prenomPers'])!.value,
      emailPers: this.editForm.get(['emailPers'])!.value,
      telPers: this.editForm.get(['telPers'])!.value,
      tel1Pers: this.editForm.get(['tel1Pers'])!.value,
      etatInscription: this.editForm.get(['etatInscription'])!.value,
      evenement: this.editForm.get(['evenement'])!.value,
    };
  }
}
