import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDomaineActivite, DomaineActivite } from '../domaine-activite.model';
import { DomaineActiviteService } from '../service/domaine-activite.service';

@Component({
  selector: 'jhi-domaine-activite-update',
  templateUrl: './domaine-activite-update.component.html',
})
export class DomaineActiviteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    typeActivite: [],
    description: [],
    dateActivite: [],
  });

  constructor(
    protected domaineActiviteService: DomaineActiviteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaineActivite }) => {
      if (domaineActivite.id === undefined) {
        const today = dayjs().startOf('day');
        domaineActivite.dateActivite = today;
      }

      this.updateForm(domaineActivite);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domaineActivite = this.createFromForm();
    if (domaineActivite.id !== undefined) {
      this.subscribeToSaveResponse(this.domaineActiviteService.update(domaineActivite));
    } else {
      this.subscribeToSaveResponse(this.domaineActiviteService.create(domaineActivite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaineActivite>>): void {
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

  protected updateForm(domaineActivite: IDomaineActivite): void {
    this.editForm.patchValue({
      id: domaineActivite.id,
      typeActivite: domaineActivite.typeActivite,
      description: domaineActivite.description,
      dateActivite: domaineActivite.dateActivite ? domaineActivite.dateActivite.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IDomaineActivite {
    return {
      ...new DomaineActivite(),
      id: this.editForm.get(['id'])!.value,
      typeActivite: this.editForm.get(['typeActivite'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateActivite: this.editForm.get(['dateActivite'])!.value
        ? dayjs(this.editForm.get(['dateActivite'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
