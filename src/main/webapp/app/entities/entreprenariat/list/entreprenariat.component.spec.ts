import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EntreprenariatService } from '../service/entreprenariat.service';

import { EntreprenariatComponent } from './entreprenariat.component';

describe('Entreprenariat Management Component', () => {
  let comp: EntreprenariatComponent;
  let fixture: ComponentFixture<EntreprenariatComponent>;
  let service: EntreprenariatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EntreprenariatComponent],
    })
      .overrideTemplate(EntreprenariatComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EntreprenariatComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EntreprenariatService);

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
    expect(comp.entreprenariats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
