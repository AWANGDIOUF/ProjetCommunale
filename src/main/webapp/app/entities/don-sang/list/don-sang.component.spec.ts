import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DonSangService } from '../service/don-sang.service';

import { DonSangComponent } from './don-sang.component';

describe('DonSang Management Component', () => {
  let comp: DonSangComponent;
  let fixture: ComponentFixture<DonSangComponent>;
  let service: DonSangService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DonSangComponent],
    })
      .overrideTemplate(DonSangComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonSangComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DonSangService);

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
    expect(comp.donSangs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
