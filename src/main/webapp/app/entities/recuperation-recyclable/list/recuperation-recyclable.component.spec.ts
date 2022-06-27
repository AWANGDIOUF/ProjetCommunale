import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RecuperationRecyclableService } from '../service/recuperation-recyclable.service';

import { RecuperationRecyclableComponent } from './recuperation-recyclable.component';

describe('RecuperationRecyclable Management Component', () => {
  let comp: RecuperationRecyclableComponent;
  let fixture: ComponentFixture<RecuperationRecyclableComponent>;
  let service: RecuperationRecyclableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RecuperationRecyclableComponent],
    })
      .overrideTemplate(RecuperationRecyclableComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecuperationRecyclableComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RecuperationRecyclableService);

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
    expect(comp.recuperationRecyclables?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
