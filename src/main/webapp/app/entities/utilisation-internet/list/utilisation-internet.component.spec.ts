import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UtilisationInternetService } from '../service/utilisation-internet.service';

import { UtilisationInternetComponent } from './utilisation-internet.component';

describe('UtilisationInternet Management Component', () => {
  let comp: UtilisationInternetComponent;
  let fixture: ComponentFixture<UtilisationInternetComponent>;
  let service: UtilisationInternetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UtilisationInternetComponent],
    })
      .overrideTemplate(UtilisationInternetComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilisationInternetComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UtilisationInternetService);

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
    expect(comp.utilisationInternets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
