import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ArtisteService } from '../service/artiste.service';

import { ArtisteComponent } from './artiste.component';

describe('Artiste Management Component', () => {
  let comp: ArtisteComponent;
  let fixture: ComponentFixture<ArtisteComponent>;
  let service: ArtisteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ArtisteComponent],
    })
      .overrideTemplate(ArtisteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArtisteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ArtisteService);

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
    expect(comp.artistes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
