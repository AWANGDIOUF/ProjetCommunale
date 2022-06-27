import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ResultatExamenService } from '../service/resultat-examen.service';

import { ResultatExamenComponent } from './resultat-examen.component';

describe('ResultatExamen Management Component', () => {
  let comp: ResultatExamenComponent;
  let fixture: ComponentFixture<ResultatExamenComponent>;
  let service: ResultatExamenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ResultatExamenComponent],
    })
      .overrideTemplate(ResultatExamenComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResultatExamenComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ResultatExamenService);

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
    expect(comp.resultatExamen?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
