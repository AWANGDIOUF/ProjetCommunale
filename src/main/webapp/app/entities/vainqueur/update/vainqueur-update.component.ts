import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVainqueur, Vainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';

@Component({
  selector: 'jhi-vainqueur-update',
  templateUrl: './vainqueur-update.component.html',
})
export class VainqueurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    prix: [],
  });

  constructor(protected vainqueurService: VainqueurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vainqueur }) => {
      this.updateForm(vainqueur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vainqueur = this.createFromForm();
    if (vainqueur.id !== undefined) {
      this.subscribeToSaveResponse(this.vainqueurService.update(vainqueur));
    } else {
      this.subscribeToSaveResponse(this.vainqueurService.create(vainqueur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVainqueur>>): void {
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

  protected updateForm(vainqueur: IVainqueur): void {
    this.editForm.patchValue({
      id: vainqueur.id,
      prix: vainqueur.prix,
    });
  }

  protected createFromForm(): IVainqueur {
    return {
      ...new Vainqueur(),
      id: this.editForm.get(['id'])!.value,
      prix: this.editForm.get(['prix'])!.value,
    };
  }
}
