import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ActivitePolitiqueService } from '../service/activite-politique.service';

import { ActivitePolitiqueComponent } from './activite-politique.component';

describe('ActivitePolitique Management Component', () => {
  let comp: ActivitePolitiqueComponent;
  let fixture: ComponentFixture<ActivitePolitiqueComponent>;
  let service: ActivitePolitiqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ActivitePolitiqueComponent],
    })
      .overrideTemplate(ActivitePolitiqueComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivitePolitiqueComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ActivitePolitiqueService);

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
    expect(comp.activitePolitiques?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
