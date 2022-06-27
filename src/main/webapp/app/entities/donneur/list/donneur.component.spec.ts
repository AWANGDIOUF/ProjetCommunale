import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DonneurService } from '../service/donneur.service';

import { DonneurComponent } from './donneur.component';

describe('Donneur Management Component', () => {
  let comp: DonneurComponent;
  let fixture: ComponentFixture<DonneurComponent>;
  let service: DonneurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DonneurComponent],
    })
      .overrideTemplate(DonneurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonneurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DonneurService);

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
    expect(comp.donneurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
