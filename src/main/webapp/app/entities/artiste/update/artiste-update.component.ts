import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IArtiste, Artiste } from '../artiste.model';
import { ArtisteService } from '../service/artiste.service';
import { Domaine } from 'app/entities/enumerations/domaine.model';

@Component({
  selector: 'jhi-artiste-update',
  templateUrl: './artiste-update.component.html',
})
export class ArtisteUpdateComponent implements OnInit {
  isSaving = false;
  domaineValues = Object.keys(Domaine);

  editForm = this.fb.group({
    id: [],
    nomArtiste: [],
    prenomArtiste: [],
    domaine: [],
    autreDomaine: [],
  });

  constructor(protected artisteService: ArtisteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artiste }) => {
      this.updateForm(artiste);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const artiste = this.createFromForm();
    if (artiste.id !== undefined) {
      this.subscribeToSaveResponse(this.artisteService.update(artiste));
    } else {
      this.subscribeToSaveResponse(this.artisteService.create(artiste));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArtiste>>): void {
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

  protected updateForm(artiste: IArtiste): void {
    this.editForm.patchValue({
      id: artiste.id,
      nomArtiste: artiste.nomArtiste,
      prenomArtiste: artiste.prenomArtiste,
      domaine: artiste.domaine,
      autreDomaine: artiste.autreDomaine,
    });
  }

  protected createFromForm(): IArtiste {
    return {
      ...new Artiste(),
      id: this.editForm.get(['id'])!.value,
      nomArtiste: this.editForm.get(['nomArtiste'])!.value,
      prenomArtiste: this.editForm.get(['prenomArtiste'])!.value,
      domaine: this.editForm.get(['domaine'])!.value,
      autreDomaine: this.editForm.get(['autreDomaine'])!.value,
    };
  }
}
