import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICombattant, Combattant } from '../combattant.model';
import { CombattantService } from '../service/combattant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { VainqueurService } from 'app/entities/vainqueur/service/vainqueur.service';

@Component({
  selector: 'jhi-combattant-update',
  templateUrl: './combattant-update.component.html',
})
export class CombattantUpdateComponent implements OnInit {
  isSaving = false;

  vainqueursSharedCollection: IVainqueur[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    dateNais: [],
    lieuNais: [],
    photo: [],
    photoContentType: [],
    combattant: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected combattantService: CombattantService,
    protected vainqueurService: VainqueurService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ combattant }) => {
      this.updateForm(combattant);

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
    const combattant = this.createFromForm();
    if (combattant.id !== undefined) {
      this.subscribeToSaveResponse(this.combattantService.update(combattant));
    } else {
      this.subscribeToSaveResponse(this.combattantService.create(combattant));
    }
  }

  trackVainqueurById(index: number, item: IVainqueur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICombattant>>): void {
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

  protected updateForm(combattant: ICombattant): void {
    this.editForm.patchValue({
      id: combattant.id,
      nom: combattant.nom,
      prenom: combattant.prenom,
      dateNais: combattant.dateNais,
      lieuNais: combattant.lieuNais,
      photo: combattant.photo,
      photoContentType: combattant.photoContentType,
      combattant: combattant.combattant,
    });

    this.vainqueursSharedCollection = this.vainqueurService.addVainqueurToCollectionIfMissing(
      this.vainqueursSharedCollection,
      combattant.combattant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vainqueurService
      .query()
      .pipe(map((res: HttpResponse<IVainqueur[]>) => res.body ?? []))
      .pipe(
        map((vainqueurs: IVainqueur[]) =>
          this.vainqueurService.addVainqueurToCollectionIfMissing(vainqueurs, this.editForm.get('combattant')!.value)
        )
      )
      .subscribe((vainqueurs: IVainqueur[]) => (this.vainqueursSharedCollection = vainqueurs));
  }

  protected createFromForm(): ICombattant {
    return {
      ...new Combattant(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNais: this.editForm.get(['dateNais'])!.value,
      lieuNais: this.editForm.get(['lieuNais'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      combattant: this.editForm.get(['combattant'])!.value,
    };
  }
}
