import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';

@Component({
  selector: 'jhi-type-vaccin-update',
  templateUrl: './type-vaccin-update.component.html',
})
export class TypeVaccinUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    objectif: [],
  });

  constructor(protected typeVaccinService: TypeVaccinService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeVaccin }) => {
      this.updateForm(typeVaccin);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeVaccin = this.createFromForm();
    if (typeVaccin.id !== undefined) {
      this.subscribeToSaveResponse(this.typeVaccinService.update(typeVaccin));
    } else {
      this.subscribeToSaveResponse(this.typeVaccinService.create(typeVaccin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeVaccin>>): void {
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

  protected updateForm(typeVaccin: ITypeVaccin): void {
    this.editForm.patchValue({
      id: typeVaccin.id,
      libelle: typeVaccin.libelle,
      objectif: typeVaccin.objectif,
    });
  }

  protected createFromForm(): ITypeVaccin {
    return {
      ...new TypeVaccin(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      objectif: this.editForm.get(['objectif'])!.value,
    };
  }
}
