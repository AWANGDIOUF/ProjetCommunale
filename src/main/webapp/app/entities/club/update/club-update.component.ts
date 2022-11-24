import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClub, Club } from '../club.model';
import { ClubService } from '../service/club.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { TypeSportService } from 'app/entities/type-sport/service/type-sport.service';
import { ICombattant } from 'app/entities/combattant/combattant.model';
import { CombattantService } from 'app/entities/combattant/service/combattant.service';

@Component({
  selector: 'jhi-club-update',
  templateUrl: './club-update.component.html',
})
export class ClubUpdateComponent implements OnInit {
  isSaving = false;

  typeSportsCollection: ITypeSport[] = [];
  combattantsSharedCollection: ICombattant[] = [];

  editForm = this.fb.group({
    id: [],
    nomClub: [],
    dateCreation: [],
    logo: [],
    logoContentType: [],
    discipline: [],
    typeSport: [],
    conmbattant: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected clubService: ClubService,
    protected typeSportService: TypeSportService,
    protected combattantService: CombattantService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ club }) => {
      this.updateForm(club);

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
    const club = this.createFromForm();
    if (club.id !== undefined) {
      this.subscribeToSaveResponse(this.clubService.update(club));
    } else {
      this.subscribeToSaveResponse(this.clubService.create(club));
    }
  }

  trackTypeSportById(index: number, item: ITypeSport): number {
    return item.id!;
  }

  trackCombattantById(index: number, item: ICombattant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClub>>): void {
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

  protected updateForm(club: IClub): void {
    this.editForm.patchValue({
      id: club.id,
      nomClub: club.nomClub,
      dateCreation: club.dateCreation,
      logo: club.logo,
      logoContentType: club.logoContentType,
      discipline: club.discipline,
      typeSport: club.typeSport,
      conmbattant: club.conmbattant,
    });

    this.typeSportsCollection = this.typeSportService.addTypeSportToCollectionIfMissing(this.typeSportsCollection, club.typeSport);
    this.combattantsSharedCollection = this.combattantService.addCombattantToCollectionIfMissing(
      this.combattantsSharedCollection,
      club.conmbattant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeSportService
      .query({ filter: 'club-is-null' })
      .pipe(map((res: HttpResponse<ITypeSport[]>) => res.body ?? []))
      .pipe(
        map((typeSports: ITypeSport[]) =>
          this.typeSportService.addTypeSportToCollectionIfMissing(typeSports, this.editForm.get('typeSport')!.value)
        )
      )
      .subscribe((typeSports: ITypeSport[]) => (this.typeSportsCollection = typeSports));

    this.combattantService
      .query()
      .pipe(map((res: HttpResponse<ICombattant[]>) => res.body ?? []))
      .pipe(
        map((combattants: ICombattant[]) =>
          this.combattantService.addCombattantToCollectionIfMissing(combattants, this.editForm.get('conmbattant')!.value)
        )
      )
      .subscribe((combattants: ICombattant[]) => (this.combattantsSharedCollection = combattants));
  }

  protected createFromForm(): IClub {
    return {
      ...new Club(),
      id: this.editForm.get(['id'])!.value,
      nomClub: this.editForm.get(['nomClub'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      discipline: this.editForm.get(['discipline'])!.value,
      typeSport: this.editForm.get(['typeSport'])!.value,
      conmbattant: this.editForm.get(['conmbattant'])!.value,
    };
  }
}
