import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VidangeService } from '../service/vidange.service';

import { VidangeComponent } from './vidange.component';

describe('Vidange Management Component', () => {
  let comp: VidangeComponent;
  let fixture: ComponentFixture<VidangeComponent>;
  let service: VidangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VidangeComponent],
    })
      .overrideTemplate(VidangeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VidangeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VidangeService);

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
    expect(comp.vidanges?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
