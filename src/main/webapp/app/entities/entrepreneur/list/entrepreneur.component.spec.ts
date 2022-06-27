import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EntrepreneurService } from '../service/entrepreneur.service';

import { EntrepreneurComponent } from './entrepreneur.component';

describe('Entrepreneur Management Component', () => {
  let comp: EntrepreneurComponent;
  let fixture: ComponentFixture<EntrepreneurComponent>;
  let service: EntrepreneurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EntrepreneurComponent],
    })
      .overrideTemplate(EntrepreneurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntrepreneurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EntrepreneurService);

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
    expect(comp.entrepreneurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
