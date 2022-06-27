import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LienTutorielService } from '../service/lien-tutoriel.service';

import { LienTutorielComponent } from './lien-tutoriel.component';

describe('LienTutoriel Management Component', () => {
  let comp: LienTutorielComponent;
  let fixture: ComponentFixture<LienTutorielComponent>;
  let service: LienTutorielService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LienTutorielComponent],
    })
      .overrideTemplate(LienTutorielComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LienTutorielComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LienTutorielService);

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
    expect(comp.lienTutoriels?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
