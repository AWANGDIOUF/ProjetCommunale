import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILienTutoriel, LienTutoriel } from '../lien-tutoriel.model';
import { LienTutorielService } from '../service/lien-tutoriel.service';
import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';
import { EnsegnantService } from 'app/entities/ensegnant/service/ensegnant.service';

@Component({
  selector: 'jhi-lien-tutoriel-update',
  templateUrl: './lien-tutoriel-update.component.html',
})
export class LienTutorielUpdateComponent implements OnInit {
  isSaving = false;

  ensegnantsSharedCollection: IEnsegnant[] = [];

  editForm = this.fb.group({
    id: [],
    descriptionLien: [],
    lien: [],
    enseignant: [],
  });

  constructor(
    protected lienTutorielService: LienTutorielService,
    protected ensegnantService: EnsegnantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lienTutoriel }) => {
      this.updateForm(lienTutoriel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lienTutoriel = this.createFromForm();
    if (lienTutoriel.id !== undefined) {
      this.subscribeToSaveResponse(this.lienTutorielService.update(lienTutoriel));
    } else {
      this.subscribeToSaveResponse(this.lienTutorielService.create(lienTutoriel));
    }
  }

  trackEnsegnantById(_index: number, item: IEnsegnant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILienTutoriel>>): void {
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

  protected updateForm(lienTutoriel: ILienTutoriel): void {
    this.editForm.patchValue({
      id: lienTutoriel.id,
      descriptionLien: lienTutoriel.descriptionLien,
      lien: lienTutoriel.lien,
      enseignant: lienTutoriel.enseignant,
    });

    this.ensegnantsSharedCollection = this.ensegnantService.addEnsegnantToCollectionIfMissing(
      this.ensegnantsSharedCollection,
      lienTutoriel.enseignant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ensegnantService
      .query()
      .pipe(map((res: HttpResponse<IEnsegnant[]>) => res.body ?? []))
      .pipe(
        map((ensegnants: IEnsegnant[]) =>
          this.ensegnantService.addEnsegnantToCollectionIfMissing(ensegnants, this.editForm.get('enseignant')!.value)
        )
      )
      .subscribe((ensegnants: IEnsegnant[]) => (this.ensegnantsSharedCollection = ensegnants));
  }

  protected createFromForm(): ILienTutoriel {
    return {
      ...new LienTutoriel(),
      id: this.editForm.get(['id'])!.value,
      descriptionLien: this.editForm.get(['descriptionLien'])!.value,
      lien: this.editForm.get(['lien'])!.value,
      enseignant: this.editForm.get(['enseignant'])!.value,
    };
  }
}
