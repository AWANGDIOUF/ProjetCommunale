import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatch } from '../match.model';
import { MatchService } from '../service/match.service';
import { MatchDeleteDialogComponent } from '../delete/match-delete-dialog.component';

@Component({
  selector: 'jhi-match',
  templateUrl: './match.component.html',
})
export class MatchComponent implements OnInit {
  matches?: IMatch[];
  isLoading = false;

  constructor(protected matchService: MatchService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.matchService.query().subscribe({
      next: (res: HttpResponse<IMatch[]>) => {
        this.isLoading = false;
        this.matches = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMatch): number {
    return item.id!;
  }

  delete(match: IMatch): void {
    const modalRef = this.modalService.open(MatchDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.match = match;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
