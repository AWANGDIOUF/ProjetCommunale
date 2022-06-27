import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEntreprenariat, Entreprenariat } from '../entreprenariat.model';
import { EntreprenariatService } from '../service/entreprenariat.service';
import { TypeEntreprenariat } from 'app/entities/enumerations/type-entreprenariat.model';

@Component({
  selector: 'jhi-entreprenariat-update',
  templateUrl: './entreprenariat-update.component.html',
})
export class EntreprenariatUpdateComponent implements OnInit {
  isSaving = false;
  typeEntreprenariatValues = Object.keys(TypeEntreprenariat);

  editForm = this.fb.group({
    id: [],
    typeEntre: [],
    autreEntre: [],
  });

  constructor(
    protected entreprenariatService: EntreprenariatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreprenariat }) => {
      this.updateForm(entreprenariat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entreprenariat = this.createFromForm();
    if (entreprenariat.id !== undefined) {
      this.subscribeToSaveResponse(this.entreprenariatService.update(entreprenariat));
    } else {
      this.subscribeToSaveResponse(this.entreprenariatService.create(entreprenariat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntreprenariat>>): void {
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

  protected updateForm(entreprenariat: IEntreprenariat): void {
    this.editForm.patchValue({
      id: entreprenariat.id,
      typeEntre: entreprenariat.typeEntre,
      autreEntre: entreprenariat.autreEntre,
    });
  }

  protected createFromForm(): IEntreprenariat {
    return {
      ...new Entreprenariat(),
      id: this.editForm.get(['id'])!.value,
      typeEntre: this.editForm.get(['typeEntre'])!.value,
      autreEntre: this.editForm.get(['autreEntre'])!.value,
    };
  }
}
