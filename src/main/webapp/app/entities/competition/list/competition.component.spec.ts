import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CompetitionService } from '../service/competition.service';

import { CompetitionComponent } from './competition.component';

describe('Competition Management Component', () => {
  let comp: CompetitionComponent;
  let fixture: ComponentFixture<CompetitionComponent>;
  let service: CompetitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CompetitionComponent],
    })
      .overrideTemplate(CompetitionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetitionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CompetitionService);

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
    expect(comp.competitions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
