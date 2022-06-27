import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISensibiisationInternet, SensibiisationInternet } from '../sensibiisation-internet.model';
import { SensibiisationInternetService } from '../service/sensibiisation-internet.service';

import { SensibiisationInternetRoutingResolveService } from './sensibiisation-internet-routing-resolve.service';

describe('SensibiisationInternet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SensibiisationInternetRoutingResolveService;
  let service: SensibiisationInternetService;
  let resultSensibiisationInternet: ISensibiisationInternet | undefined;

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
    routingResolveService = TestBed.inject(SensibiisationInternetRoutingResolveService);
    service = TestBed.inject(SensibiisationInternetService);
    resultSensibiisationInternet = undefined;
  });

  describe('resolve', () => {
    it('should return ISensibiisationInternet returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensibiisationInternet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensibiisationInternet).toEqual({ id: 123 });
    });

    it('should return new ISensibiisationInternet if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensibiisationInternet = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSensibiisationInternet).toEqual(new SensibiisationInternet());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SensibiisationInternet })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensibiisationInternet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensibiisationInternet).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
