import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CollecteurOdeurService } from '../service/collecteur-odeur.service';

import { CollecteurOdeurComponent } from './collecteur-odeur.component';

describe('CollecteurOdeur Management Component', () => {
  let comp: CollecteurOdeurComponent;
  let fixture: ComponentFixture<CollecteurOdeurComponent>;
  let service: CollecteurOdeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CollecteurOdeurComponent],
    })
      .overrideTemplate(CollecteurOdeurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollecteurOdeurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CollecteurOdeurService);

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
    expect(comp.collecteurOdeurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
