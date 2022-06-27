import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDon, Don } from '../don.model';
import { DonService } from '../service/don.service';
import { IDonneur } from 'app/entities/donneur/donneur.model';
import { DonneurService } from 'app/entities/donneur/service/donneur.service';
import { TypeDon } from 'app/entities/enumerations/type-don.model';

@Component({
  selector: 'jhi-don-update',
  templateUrl: './don-update.component.html',
})
export class DonUpdateComponent implements OnInit {
  isSaving = false;
  typeDonValues = Object.keys(TypeDon);

  donneursSharedCollection: IDonneur[] = [];

  editForm = this.fb.group({
    id: [],
    typeDon: [],
    montant: [],
    description: [],
    donneur: [],
  });

  constructor(
    protected donService: DonService,
    protected donneurService: DonneurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ don }) => {
      this.updateForm(don);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const don = this.createFromForm();
    if (don.id !== undefined) {
      this.subscribeToSaveResponse(this.donService.update(don));
    } else {
      this.subscribeToSaveResponse(this.donService.create(don));
    }
  }

  trackDonneurById(_index: number, item: IDonneur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDon>>): void {
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

  protected updateForm(don: IDon): void {
    this.editForm.patchValue({
      id: don.id,
      typeDon: don.typeDon,
      montant: don.montant,
      description: don.description,
      donneur: don.donneur,
    });

    this.donneursSharedCollection = this.donneurService.addDonneurToCollectionIfMissing(this.donneursSharedCollection, don.donneur);
  }

  protected loadRelationshipsOptions(): void {
    this.donneurService
      .query()
      .pipe(map((res: HttpResponse<IDonneur[]>) => res.body ?? []))
      .pipe(
        map((donneurs: IDonneur[]) => this.donneurService.addDonneurToCollectionIfMissing(donneurs, this.editForm.get('donneur')!.value))
      )
      .subscribe((donneurs: IDonneur[]) => (this.donneursSharedCollection = donneurs));
  }

  protected createFromForm(): IDon {
    return {
      ...new Don(),
      id: this.editForm.get(['id'])!.value,
      typeDon: this.editForm.get(['typeDon'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      description: this.editForm.get(['description'])!.value,
      donneur: this.editForm.get(['donneur'])!.value,
    };
  }
}
