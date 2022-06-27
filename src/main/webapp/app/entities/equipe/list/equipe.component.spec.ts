import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EquipeService } from '../service/equipe.service';

import { EquipeComponent } from './equipe.component';

describe('Equipe Management Component', () => {
  let comp: EquipeComponent;
  let fixture: ComponentFixture<EquipeComponent>;
  let service: EquipeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EquipeComponent],
    })
      .overrideTemplate(EquipeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EquipeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EquipeService);

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
    expect(comp.equipes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
