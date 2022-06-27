import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypeVaccinService } from '../service/type-vaccin.service';

import { TypeVaccinComponent } from './type-vaccin.component';

describe('TypeVaccin Management Component', () => {
  let comp: TypeVaccinComponent;
  let fixture: ComponentFixture<TypeVaccinComponent>;
  let service: TypeVaccinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeVaccinComponent],
    })
      .overrideTemplate(TypeVaccinComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeVaccinComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeVaccinService);

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
    expect(comp.typeVaccins?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
