import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEquipe, Equipe } from '../equipe.model';
import { EquipeService } from '../service/equipe.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

@Component({
  selector: 'jhi-equipe-update',
  templateUrl: './equipe-update.component.html',
})
export class EquipeUpdateComponent implements OnInit {
  isSaving = false;

  typeSportsCollection: ITypeSport[] = [];
  quartiersSharedCollection: IQuartier[] = [];

  editForm = this.fb.group({
    id: [],
    nomEquipe: [],
    dateCreation: [],
    logo: [],
    logoContentType: [],
    typeSport: [],
    quartier: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected equipeService: EquipeService,
    protected typeSportService: TypeSportService,
    protected quartierService: QuartierService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipe }) => {
      this.updateForm(equipe);

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
    const equipe = this.createFromForm();
    if (equipe.id !== undefined) {
      this.subscribeToSaveResponse(this.equipeService.update(equipe));
    } else {
      this.subscribeToSaveResponse(this.equipeService.create(equipe));
    }
  }

  trackTypeSportById(_index: number, item: ITypeSport): number {
    return item.id!;
  }

  trackQuartierById(_index: number, item: IQuartier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipe>>): void {
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

  protected updateForm(equipe: IEquipe): void {
    this.editForm.patchValue({
      id: equipe.id,
      nomEquipe: equipe.nomEquipe,
      dateCreation: equipe.dateCreation,
      logo: equipe.logo,
      logoContentType: equipe.logoContentType,
      typeSport: equipe.typeSport,
      quartier: equipe.quartier,
    });

    this.typeSportsCollection = this.typeSportService.addTypeSportToCollectionIfMissing(this.typeSportsCollection, equipe.typeSport);
    this.quartiersSharedCollection = this.quartierService.addQuartierToCollectionIfMissing(this.quartiersSharedCollection, equipe.quartier);
  }

  protected loadRelationshipsOptions(): void {
    this.typeSportService
      .query({ filter: 'equipe-is-null' })
      .pipe(map((res: HttpResponse<ITypeSport[]>) => res.body ?? []))
      .pipe(
        map((typeSports: ITypeSport[]) =>
          this.typeSportService.addTypeSportToCollectionIfMissing(typeSports, this.editForm.get('typeSport')!.value)
        )
      )
      .subscribe((typeSports: ITypeSport[]) => (this.typeSportsCollection = typeSports));

    this.quartierService
      .query()
      .pipe(map((res: HttpResponse<IQuartier[]>) => res.body ?? []))
      .pipe(
        map((quartiers: IQuartier[]) =>
          this.quartierService.addQuartierToCollectionIfMissing(quartiers, this.editForm.get('quartier')!.value)
        )
      )
      .subscribe((quartiers: IQuartier[]) => (this.quartiersSharedCollection = quartiers));
  }

  protected createFromForm(): IEquipe {
    return {
      ...new Equipe(),
      id: this.editForm.get(['id'])!.value,
      nomEquipe: this.editForm.get(['nomEquipe'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      typeSport: this.editForm.get(['typeSport'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
    };
  }
}
