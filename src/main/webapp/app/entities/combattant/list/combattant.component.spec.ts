import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CombattantService } from '../service/combattant.service';

import { CombattantComponent } from './combattant.component';

describe('Combattant Management Component', () => {
  let comp: CombattantComponent;
  let fixture: ComponentFixture<CombattantComponent>;
  let service: CombattantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CombattantComponent],
    })
      .overrideTemplate(CombattantComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CombattantComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CombattantService);

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
    expect(comp.combattants?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
