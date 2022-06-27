import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDon } from '../don.model';

@Component({
  selector: 'jhi-don-detail',
  templateUrl: './don-detail.component.html',
})
export class DonDetailComponent implements OnInit {
  don: IDon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ don }) => {
      this.don = don;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
