import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IJoueur, Joueur } from '../joueur.model';
import { JoueurService } from '../service/joueur.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-joueur-update',
  templateUrl: './joueur-update.component.html',
})
export class JoueurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    dateNais: [],
    lieuNais: [],
    poste: [],
    photo: [],
    photoContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected joueurService: JoueurService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ joueur }) => {
      this.updateForm(joueur);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('projetCommunalApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const joueur = this.createFromForm();
    if (joueur.id !== undefined) {
      this.subscribeToSaveResponse(this.joueurService.update(joueur));
    } else {
      this.subscribeToSaveResponse(this.joueurService.create(joueur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJoueur>>): void {
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

  protected updateForm(joueur: IJoueur): void {
    this.editForm.patchValue({
      id: joueur.id,
      nom: joueur.nom,
      prenom: joueur.prenom,
      dateNais: joueur.dateNais,
      lieuNais: joueur.lieuNais,
      poste: joueur.poste,
      photo: joueur.photo,
      photoContentType: joueur.photoContentType,
    });
  }

  protected createFromForm(): IJoueur {
    return {
      ...new Joueur(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNais: this.editForm.get(['dateNais'])!.value,
      lieuNais: this.editForm.get(['lieuNais'])!.value,
      poste: this.editForm.get(['poste'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
    };
  }
}
