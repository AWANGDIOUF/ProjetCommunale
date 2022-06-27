import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEntreprenariat, Entreprenariat } from '../entreprenariat.model';
import { EntreprenariatService } from '../service/entreprenariat.service';

import { EntreprenariatRoutingResolveService } from './entreprenariat-routing-resolve.service';

describe('Entreprenariat routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EntreprenariatRoutingResolveService;
  let service: EntreprenariatService;
  let resultEntreprenariat: IEntreprenariat | undefined;

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
    routingResolveService = TestBed.inject(EntreprenariatRoutingResolveService);
    service = TestBed.inject(EntreprenariatService);
    resultEntreprenariat = undefined;
  });

  describe('resolve', () => {
    it('should return IEntreprenariat returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprenariat = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntreprenariat).toEqual({ id: 123 });
    });

    it('should return new IEntreprenariat if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprenariat = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEntreprenariat).toEqual(new Entreprenariat());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Entreprenariat })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprenariat = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntreprenariat).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
