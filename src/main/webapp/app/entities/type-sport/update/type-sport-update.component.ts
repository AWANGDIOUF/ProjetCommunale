import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeSport, TypeSport } from '../type-sport.model';
import { TypeSportService } from '../service/type-sport.service';
import { Sport } from 'app/entities/enumerations/sport.model';

@Component({
  selector: 'jhi-type-sport-update',
  templateUrl: './type-sport-update.component.html',
})
export class TypeSportUpdateComponent implements OnInit {
  isSaving = false;
  sportValues = Object.keys(Sport);

  editForm = this.fb.group({
    id: [],
    sport: [],
  });

  constructor(protected typeSportService: TypeSportService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSport }) => {
      this.updateForm(typeSport);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeSport = this.createFromForm();
    if (typeSport.id !== undefined) {
      this.subscribeToSaveResponse(this.typeSportService.update(typeSport));
    } else {
      this.subscribeToSaveResponse(this.typeSportService.create(typeSport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeSport>>): void {
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

  protected updateForm(typeSport: ITypeSport): void {
    this.editForm.patchValue({
      id: typeSport.id,
      sport: typeSport.sport,
    });
  }

  protected createFromForm(): ITypeSport {
    return {
      ...new TypeSport(),
      id: this.editForm.get(['id'])!.value,
      sport: this.editForm.get(['sport'])!.value,
    };
  }
}
