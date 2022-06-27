import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EleveurService } from '../service/eleveur.service';

import { EleveurComponent } from './eleveur.component';

describe('Eleveur Management Component', () => {
  let comp: EleveurComponent;
  let fixture: ComponentFixture<EleveurComponent>;
  let service: EleveurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EleveurComponent],
    })
      .overrideTemplate(EleveurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EleveurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EleveurService);

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
    expect(comp.eleveurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
