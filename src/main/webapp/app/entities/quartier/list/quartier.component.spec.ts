import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuartierService } from '../service/quartier.service';

import { QuartierComponent } from './quartier.component';

describe('Quartier Management Component', () => {
  let comp: QuartierComponent;
  let fixture: ComponentFixture<QuartierComponent>;
  let service: QuartierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [QuartierComponent],
    })
      .overrideTemplate(QuartierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuartierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuartierService);

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
    expect(comp.quartiers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
