import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUtilisationInternet, UtilisationInternet } from '../utilisation-internet.model';
import { UtilisationInternetService } from '../service/utilisation-internet.service';

import { UtilisationInternetRoutingResolveService } from './utilisation-internet-routing-resolve.service';

describe('UtilisationInternet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UtilisationInternetRoutingResolveService;
  let service: UtilisationInternetService;
  let resultUtilisationInternet: IUtilisationInternet | undefined;

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
    routingResolveService = TestBed.inject(UtilisationInternetRoutingResolveService);
    service = TestBed.inject(UtilisationInternetService);
    resultUtilisationInternet = undefined;
  });

  describe('resolve', () => {
    it('should return IUtilisationInternet returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUtilisationInternet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUtilisationInternet).toEqual({ id: 123 });
    });

    it('should return new IUtilisationInternet if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUtilisationInternet = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUtilisationInternet).toEqual(new UtilisationInternet());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UtilisationInternet })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUtilisationInternet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUtilisationInternet).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
