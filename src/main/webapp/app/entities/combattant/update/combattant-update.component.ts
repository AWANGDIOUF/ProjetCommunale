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
import { IClub } from 'app/entities/club/club.model';
import { ClubService } from 'app/entities/club/service/club.service';

@Component({
  selector: 'jhi-combattant-update',
  templateUrl: './combattant-update.component.html',
})
export class CombattantUpdateComponent implements OnInit {
  isSaving = false;

  clubsSharedCollection: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    nomCombattant: [],
    prenomCombattant: [],
    dateNaisCombattant: [],
    lieuNaisCombattant: [],
    photoCombattant: [],
    photoCombattantContentType: [],
    club: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected combattantService: CombattantService,
    protected clubService: ClubService,
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
    const combattant = this.createFromForm();
    if (combattant.id !== undefined) {
      this.subscribeToSaveResponse(this.combattantService.update(combattant));
    } else {
      this.subscribeToSaveResponse(this.combattantService.create(combattant));
    }
  }

  trackClubById(_index: number, item: IClub): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICombattant>>): void {
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

  protected updateForm(combattant: ICombattant): void {
    this.editForm.patchValue({
      id: combattant.id,
      nomCombattant: combattant.nomCombattant,
      prenomCombattant: combattant.prenomCombattant,
      dateNaisCombattant: combattant.dateNaisCombattant,
      lieuNaisCombattant: combattant.lieuNaisCombattant,
      photoCombattant: combattant.photoCombattant,
      photoCombattantContentType: combattant.photoCombattantContentType,
      club: combattant.club,
    });

    this.clubsSharedCollection = this.clubService.addClubToCollectionIfMissing(this.clubsSharedCollection, combattant.club);
  }

  protected loadRelationshipsOptions(): void {
    this.clubService
      .query()
      .pipe(map((res: HttpResponse<IClub[]>) => res.body ?? []))
      .pipe(map((clubs: IClub[]) => this.clubService.addClubToCollectionIfMissing(clubs, this.editForm.get('club')!.value)))
      .subscribe((clubs: IClub[]) => (this.clubsSharedCollection = clubs));
  }

  protected createFromForm(): ICombattant {
    return {
      ...new Combattant(),
      id: this.editForm.get(['id'])!.value,
      nomCombattant: this.editForm.get(['nomCombattant'])!.value,
      prenomCombattant: this.editForm.get(['prenomCombattant'])!.value,
      dateNaisCombattant: this.editForm.get(['dateNaisCombattant'])!.value,
      lieuNaisCombattant: this.editForm.get(['lieuNaisCombattant'])!.value,
      photoCombattantContentType: this.editForm.get(['photoCombattantContentType'])!.value,
      photoCombattant: this.editForm.get(['photoCombattant'])!.value,
      club: this.editForm.get(['club'])!.value,
    };
  }
}
