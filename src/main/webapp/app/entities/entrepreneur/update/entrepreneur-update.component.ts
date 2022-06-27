import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEntrepreneur, Entrepreneur } from '../entrepreneur.model';
import { EntrepreneurService } from '../service/entrepreneur.service';
import { IEntreprenariat } from 'app/entities/entreprenariat/entreprenariat.model';
import { EntreprenariatService } from 'app/entities/entreprenariat/service/entreprenariat.service';
import { IDomaineActivite } from 'app/entities/domaine-activite/domaine-activite.model';
import { DomaineActiviteService } from 'app/entities/domaine-activite/service/domaine-activite.service';

@Component({
  selector: 'jhi-entrepreneur-update',
  templateUrl: './entrepreneur-update.component.html',
})
export class EntrepreneurUpdateComponent implements OnInit {
  isSaving = false;

  entreprenariatsCollection: IEntreprenariat[] = [];
  domaineActivitesCollection: IDomaineActivite[] = [];

  editForm = this.fb.group({
    id: [],
    nomEntrepreneur: [null, [Validators.required]],
    prenomEntrepreneur: [],
    emailEntrepreneur: [null, []],
    telEntrepreneur: [null, [Validators.required]],
    tel1Entrepreneur: [null, []],
    entreprenariat: [],
    domaineActivite: [],
  });

  constructor(
    protected entrepreneurService: EntrepreneurService,
    protected entreprenariatService: EntreprenariatService,
    protected domaineActiviteService: DomaineActiviteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entrepreneur }) => {
      this.updateForm(entrepreneur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entrepreneur = this.createFromForm();
    if (entrepreneur.id !== undefined) {
      this.subscribeToSaveResponse(this.entrepreneurService.update(entrepreneur));
    } else {
      this.subscribeToSaveResponse(this.entrepreneurService.create(entrepreneur));
    }
  }

  trackEntreprenariatById(_index: number, item: IEntreprenariat): number {
    return item.id!;
  }

  trackDomaineActiviteById(_index: number, item: IDomaineActivite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntrepreneur>>): void {
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

  protected updateForm(entrepreneur: IEntrepreneur): void {
    this.editForm.patchValue({
      id: entrepreneur.id,
      nomEntrepreneur: entrepreneur.nomEntrepreneur,
      prenomEntrepreneur: entrepreneur.prenomEntrepreneur,
      emailEntrepreneur: entrepreneur.emailEntrepreneur,
      telEntrepreneur: entrepreneur.telEntrepreneur,
      tel1Entrepreneur: entrepreneur.tel1Entrepreneur,
      entreprenariat: entrepreneur.entreprenariat,
      domaineActivite: entrepreneur.domaineActivite,
    });

    this.entreprenariatsCollection = this.entreprenariatService.addEntreprenariatToCollectionIfMissing(
      this.entreprenariatsCollection,
      entrepreneur.entreprenariat
    );
    this.domaineActivitesCollection = this.domaineActiviteService.addDomaineActiviteToCollectionIfMissing(
      this.domaineActivitesCollection,
      entrepreneur.domaineActivite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.entreprenariatService
      .query({ filter: 'entrepreneur-is-null' })
      .pipe(map((res: HttpResponse<IEntreprenariat[]>) => res.body ?? []))
      .pipe(
        map((entreprenariats: IEntreprenariat[]) =>
          this.entreprenariatService.addEntreprenariatToCollectionIfMissing(entreprenariats, this.editForm.get('entreprenariat')!.value)
        )
      )
      .subscribe((entreprenariats: IEntreprenariat[]) => (this.entreprenariatsCollection = entreprenariats));

    this.domaineActiviteService
      .query({ filter: 'entrepreneur-is-null' })
      .pipe(map((res: HttpResponse<IDomaineActivite[]>) => res.body ?? []))
      .pipe(
        map((domaineActivites: IDomaineActivite[]) =>
          this.domaineActiviteService.addDomaineActiviteToCollectionIfMissing(domaineActivites, this.editForm.get('domaineActivite')!.value)
        )
      )
      .subscribe((domaineActivites: IDomaineActivite[]) => (this.domaineActivitesCollection = domaineActivites));
  }

  protected createFromForm(): IEntrepreneur {
    return {
      ...new Entrepreneur(),
      id: this.editForm.get(['id'])!.value,
      nomEntrepreneur: this.editForm.get(['nomEntrepreneur'])!.value,
      prenomEntrepreneur: this.editForm.get(['prenomEntrepreneur'])!.value,
      emailEntrepreneur: this.editForm.get(['emailEntrepreneur'])!.value,
      telEntrepreneur: this.editForm.get(['telEntrepreneur'])!.value,
      tel1Entrepreneur: this.editForm.get(['tel1Entrepreneur'])!.value,
      entreprenariat: this.editForm.get(['entreprenariat'])!.value,
      domaineActivite: this.editForm.get(['domaineActivite'])!.value,
    };
  }
}
