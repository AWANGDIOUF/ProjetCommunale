import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EtablissementService } from '../service/etablissement.service';

import { EtablissementComponent } from './etablissement.component';

describe('Etablissement Management Component', () => {
  let comp: EtablissementComponent;
  let fixture: ComponentFixture<EtablissementComponent>;
  let service: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EtablissementComponent],
    })
      .overrideTemplate(EtablissementComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtablissementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EtablissementService);

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
    expect(comp.etablissements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
