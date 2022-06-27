import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CibleService } from '../service/cible.service';

import { CibleComponent } from './cible.component';

describe('Cible Management Component', () => {
  let comp: CibleComponent;
  let fixture: ComponentFixture<CibleComponent>;
  let service: CibleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CibleComponent],
    })
      .overrideTemplate(CibleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CibleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CibleService);

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
    expect(comp.cibles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
