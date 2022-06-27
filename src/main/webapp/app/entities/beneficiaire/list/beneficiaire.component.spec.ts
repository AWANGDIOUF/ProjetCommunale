import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BeneficiaireService } from '../service/beneficiaire.service';

import { BeneficiaireComponent } from './beneficiaire.component';

describe('Beneficiaire Management Component', () => {
  let comp: BeneficiaireComponent;
  let fixture: ComponentFixture<BeneficiaireComponent>;
  let service: BeneficiaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BeneficiaireComponent],
    })
      .overrideTemplate(BeneficiaireComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeneficiaireComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BeneficiaireService);

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
    expect(comp.beneficiaires?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
