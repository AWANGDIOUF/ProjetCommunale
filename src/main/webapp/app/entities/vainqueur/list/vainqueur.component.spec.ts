import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { VainqueurService } from '../service/vainqueur.service';

import { VainqueurComponent } from './vainqueur.component';

describe('Vainqueur Management Component', () => {
  let comp: VainqueurComponent;
  let fixture: ComponentFixture<VainqueurComponent>;
  let service: VainqueurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VainqueurComponent],
    })
      .overrideTemplate(VainqueurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VainqueurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VainqueurService);

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
    expect(comp.vainqueurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
