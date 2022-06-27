import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILienTutoriel, LienTutoriel } from '../lien-tutoriel.model';
import { LienTutorielService } from '../service/lien-tutoriel.service';

import { LienTutorielRoutingResolveService } from './lien-tutoriel-routing-resolve.service';

describe('LienTutoriel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LienTutorielRoutingResolveService;
  let service: LienTutorielService;
  let resultLienTutoriel: ILienTutoriel | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(LienTutorielRoutingResolveService);
    service = TestBed.inject(LienTutorielService);
    resultLienTutoriel = undefined;
  });

  describe('resolve', () => {
    it('should return ILienTutoriel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLienTutoriel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLienTutoriel).toEqual({ id: 123 });
    });

    it('should return new ILienTutoriel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLienTutoriel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLienTutoriel).toEqual(new LienTutoriel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LienTutoriel })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLienTutoriel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLienTutoriel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
