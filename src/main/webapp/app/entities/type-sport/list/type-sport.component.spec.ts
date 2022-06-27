import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypeSportService } from '../service/type-sport.service';

import { TypeSportComponent } from './type-sport.component';

describe('TypeSport Management Component', () => {
  let comp: TypeSportComponent;
  let fixture: ComponentFixture<TypeSportComponent>;
  let service: TypeSportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeSportComponent],
    })
      .overrideTemplate(TypeSportComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeSportComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeSportService);

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
    expect(comp.typeSports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
