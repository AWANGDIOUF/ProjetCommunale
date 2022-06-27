import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeInterview } from '../demande-interview.model';

@Component({
  selector: 'jhi-demande-interview-detail',
  templateUrl: './demande-interview-detail.component.html',
})
export class DemandeInterviewDetailComponent implements OnInit {
  demandeInterview: IDemandeInterview | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInterview }) => {
      this.demandeInterview = demandeInterview;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
