import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';
import { IVaccination } from 'app/entities/vaccination/vaccination.model';
import { VaccinationService } from 'app/entities/vaccination/service/vaccination.service';

@Component({
  selector: 'jhi-type-vaccin-update',
  templateUrl: './type-vaccin-update.component.html',
})
export class TypeVaccinUpdateComponent implements OnInit {
  isSaving = false;

  vaccinationsCollection: IVaccination[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    vaccination: [],
  });

  constructor(
    protected typeVaccinService: TypeVaccinService,
    protected vaccinationService: VaccinationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeVaccin }) => {
      this.updateForm(typeVaccin);

      this.loadRelationshipsOptions();
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

  trackVaccinationById(index: number, item: IVaccination): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeVaccin>>): void {
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

  protected updateForm(typeVaccin: ITypeVaccin): void {
    this.editForm.patchValue({
      id: typeVaccin.id,
      libelle: typeVaccin.libelle,
      vaccination: typeVaccin.vaccination,
    });

    this.vaccinationsCollection = this.vaccinationService.addVaccinationToCollectionIfMissing(
      this.vaccinationsCollection,
      typeVaccin.vaccination
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vaccinationService
      .query({ filter: 'typevaccin-is-null' })
      .pipe(map((res: HttpResponse<IVaccination[]>) => res.body ?? []))
      .pipe(
        map((vaccinations: IVaccination[]) =>
          this.vaccinationService.addVaccinationToCollectionIfMissing(vaccinations, this.editForm.get('vaccination')!.value)
        )
      )
      .subscribe((vaccinations: IVaccination[]) => (this.vaccinationsCollection = vaccinations));
  }

  protected createFromForm(): ITypeVaccin {
    return {
      ...new TypeVaccin(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      vaccination: this.editForm.get(['vaccination'])!.value,
    };
  }
}
