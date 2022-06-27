import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICible, Cible } from '../cible.model';
import { CibleService } from '../service/cible.service';
import { IVaccination } from 'app/entities/vaccination/vaccination.model';
import { VaccinationService } from 'app/entities/vaccination/service/vaccination.service';
import { CibleVacc } from 'app/entities/enumerations/cible-vacc.model';

@Component({
  selector: 'jhi-cible-update',
  templateUrl: './cible-update.component.html',
})
export class CibleUpdateComponent implements OnInit {
  isSaving = false;
  cibleVaccValues = Object.keys(CibleVacc);

  vaccinationsSharedCollection: IVaccination[] = [];

  editForm = this.fb.group({
    id: [],
    cible: [],
    age: [],
    vaccination: [],
  });

  constructor(
    protected cibleService: CibleService,
    protected vaccinationService: VaccinationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cible }) => {
      this.updateForm(cible);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cible = this.createFromForm();
    if (cible.id !== undefined) {
      this.subscribeToSaveResponse(this.cibleService.update(cible));
    } else {
      this.subscribeToSaveResponse(this.cibleService.create(cible));
    }
  }

  trackVaccinationById(_index: number, item: IVaccination): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICible>>): void {
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

  protected updateForm(cible: ICible): void {
    this.editForm.patchValue({
      id: cible.id,
      cible: cible.cible,
      age: cible.age,
      vaccination: cible.vaccination,
    });

    this.vaccinationsSharedCollection = this.vaccinationService.addVaccinationToCollectionIfMissing(
      this.vaccinationsSharedCollection,
      cible.vaccination
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vaccinationService
      .query()
      .pipe(map((res: HttpResponse<IVaccination[]>) => res.body ?? []))
      .pipe(
        map((vaccinations: IVaccination[]) =>
          this.vaccinationService.addVaccinationToCollectionIfMissing(vaccinations, this.editForm.get('vaccination')!.value)
        )
      )
      .subscribe((vaccinations: IVaccination[]) => (this.vaccinationsSharedCollection = vaccinations));
  }

  protected createFromForm(): ICible {
    return {
      ...new Cible(),
      id: this.editForm.get(['id'])!.value,
      cible: this.editForm.get(['cible'])!.value,
      age: this.editForm.get(['age'])!.value,
      vaccination: this.editForm.get(['vaccination'])!.value,
    };
  }
}
