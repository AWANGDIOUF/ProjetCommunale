import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PropositionService } from '../service/proposition.service';

import { PropositionComponent } from './proposition.component';

describe('Proposition Management Component', () => {
  let comp: PropositionComponent;
  let fixture: ComponentFixture<PropositionComponent>;
  let service: PropositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PropositionComponent],
    })
      .overrideTemplate(PropositionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropositionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PropositionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.propositions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
