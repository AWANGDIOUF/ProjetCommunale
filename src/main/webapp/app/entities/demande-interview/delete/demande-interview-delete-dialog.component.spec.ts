jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DemandeInterviewService } from '../service/demande-interview.service';

import { DemandeInterviewDeleteDialogComponent } from './demande-interview-delete-dialog.component';

describe('DemandeInterview Management Delete Component', () => {
  let comp: DemandeInterviewDeleteDialogComponent;
  let fixture: ComponentFixture<DemandeInterviewDeleteDialogComponent>;
  let service: DemandeInterviewService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DemandeInterviewDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(DemandeInterviewDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeInterviewDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DemandeInterviewService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
