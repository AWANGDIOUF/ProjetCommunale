import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IJoueur, Joueur } from '../joueur.model';
import { JoueurService } from '../service/joueur.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { EquipeService } from 'app/entities/equipe/service/equipe.service';
import { Poste } from 'app/entities/enumerations/poste.model';

@Component({
  selector: 'jhi-joueur-update',
  templateUrl: './joueur-update.component.html',
})
export class JoueurUpdateComponent implements OnInit {
  isSaving = false;
  posteValues = Object.keys(Poste);

  equipesSharedCollection: IEquipe[] = [];

  editForm = this.fb.group({
    id: [],
    nomJoueur: [],
    prenomJoueur: [],
    dateNaisJoueur: [],
    lieuNaisJoueur: [],
    posteJoueur: [],
    photoJoueur: [],
    photoJoueurContentType: [],
    equipe: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected joueurService: JoueurService,
    protected equipeService: EquipeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ joueur }) => {
      this.updateForm(joueur);

      this.loadRelationshipsOptions();
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('projetCommunaleApp.error', { ...err, key: 'error.file.' + err.key })),
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

  trackEquipeById(_index: number, item: IEquipe): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJoueur>>): void {
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

  protected updateForm(joueur: IJoueur): void {
    this.editForm.patchValue({
      id: joueur.id,
      nomJoueur: joueur.nomJoueur,
      prenomJoueur: joueur.prenomJoueur,
      dateNaisJoueur: joueur.dateNaisJoueur,
      lieuNaisJoueur: joueur.lieuNaisJoueur,
      posteJoueur: joueur.posteJoueur,
      photoJoueur: joueur.photoJoueur,
      photoJoueurContentType: joueur.photoJoueurContentType,
      equipe: joueur.equipe,
    });

    this.equipesSharedCollection = this.equipeService.addEquipeToCollectionIfMissing(this.equipesSharedCollection, joueur.equipe);
  }

  protected loadRelationshipsOptions(): void {
    this.equipeService
      .query()
      .pipe(map((res: HttpResponse<IEquipe[]>) => res.body ?? []))
      .pipe(map((equipes: IEquipe[]) => this.equipeService.addEquipeToCollectionIfMissing(equipes, this.editForm.get('equipe')!.value)))
      .subscribe((equipes: IEquipe[]) => (this.equipesSharedCollection = equipes));
  }

  protected createFromForm(): IJoueur {
    return {
      ...new Joueur(),
      id: this.editForm.get(['id'])!.value,
      nomJoueur: this.editForm.get(['nomJoueur'])!.value,
      prenomJoueur: this.editForm.get(['prenomJoueur'])!.value,
      dateNaisJoueur: this.editForm.get(['dateNaisJoueur'])!.value,
      lieuNaisJoueur: this.editForm.get(['lieuNaisJoueur'])!.value,
      posteJoueur: this.editForm.get(['posteJoueur'])!.value,
      photoJoueurContentType: this.editForm.get(['photoJoueurContentType'])!.value,
      photoJoueur: this.editForm.get(['photoJoueur'])!.value,
      equipe: this.editForm.get(['equipe'])!.value,
    };
  }
}
