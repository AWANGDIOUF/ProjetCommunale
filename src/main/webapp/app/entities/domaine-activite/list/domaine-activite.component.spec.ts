import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DomaineActiviteService } from '../service/domaine-activite.service';

import { DomaineActiviteComponent } from './domaine-activite.component';

describe('DomaineActivite Management Component', () => {
  let comp: DomaineActiviteComponent;
  let fixture: ComponentFixture<DomaineActiviteComponent>;
  let service: DomaineActiviteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DomaineActiviteComponent],
    })
      .overrideTemplate(DomaineActiviteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomaineActiviteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DomaineActiviteService);

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
    expect(comp.domaineActivites?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
