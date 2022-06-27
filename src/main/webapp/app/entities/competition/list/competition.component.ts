import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';
import { CompetitionDeleteDialogComponent } from '../delete/competition-delete-dialog.component';

@Component({
  selector: 'jhi-competition',
  templateUrl: './competition.component.html',
})
export class CompetitionComponent implements OnInit {
  competitions?: ICompetition[];
  isLoading = false;

  constructor(protected competitionService: CompetitionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.competitionService.query().subscribe({
      next: (res: HttpResponse<ICompetition[]>) => {
        this.isLoading = false;
        this.competitions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICompetition): number {
    return item.id!;
  }

  delete(competition: ICompetition): void {
    const modalRef = this.modalService.open(CompetitionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.competition = competition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
