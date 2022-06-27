import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInterviewsArtiste, InterviewsArtiste } from '../interviews-artiste.model';
import { InterviewsArtisteService } from '../service/interviews-artiste.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IArtiste } from 'app/entities/artiste/artiste.model';
import { ArtisteService } from 'app/entities/artiste/service/artiste.service';

@Component({
  selector: 'jhi-interviews-artiste-update',
  templateUrl: './interviews-artiste-update.component.html',
})
export class InterviewsArtisteUpdateComponent implements OnInit {
  isSaving = false;

  artistesCollection: IArtiste[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [],
    description: [],
    lien: [],
    artiste: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected interviewsArtisteService: InterviewsArtisteService,
    protected artisteService: ArtisteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewsArtiste }) => {
      this.updateForm(interviewsArtiste);

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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interviewsArtiste = this.createFromForm();
    if (interviewsArtiste.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewsArtisteService.update(interviewsArtiste));
    } else {
      this.subscribeToSaveResponse(this.interviewsArtisteService.create(interviewsArtiste));
    }
  }

  trackArtisteById(_index: number, item: IArtiste): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewsArtiste>>): void {
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

  protected updateForm(interviewsArtiste: IInterviewsArtiste): void {
    this.editForm.patchValue({
      id: interviewsArtiste.id,
      titre: interviewsArtiste.titre,
      description: interviewsArtiste.description,
      lien: interviewsArtiste.lien,
      artiste: interviewsArtiste.artiste,
    });

    this.artistesCollection = this.artisteService.addArtisteToCollectionIfMissing(this.artistesCollection, interviewsArtiste.artiste);
  }

  protected loadRelationshipsOptions(): void {
    this.artisteService
      .query({ filter: 'interviewsartiste-is-null' })
      .pipe(map((res: HttpResponse<IArtiste[]>) => res.body ?? []))
      .pipe(
        map((artistes: IArtiste[]) => this.artisteService.addArtisteToCollectionIfMissing(artistes, this.editForm.get('artiste')!.value))
      )
      .subscribe((artistes: IArtiste[]) => (this.artistesCollection = artistes));
  }

  protected createFromForm(): IInterviewsArtiste {
    return {
      ...new InterviewsArtiste(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      description: this.editForm.get(['description'])!.value,
      lien: this.editForm.get(['lien'])!.value,
      artiste: this.editForm.get(['artiste'])!.value,
    };
  }
}
