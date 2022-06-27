import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IActivitePolitique, ActivitePolitique } from '../activite-politique.model';
import { ActivitePolitiqueService } from '../service/activite-politique.service';

import { ActivitePolitiqueRoutingResolveService } from './activite-politique-routing-resolve.service';

describe('ActivitePolitique routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ActivitePolitiqueRoutingResolveService;
  let service: ActivitePolitiqueService;
  let resultActivitePolitique: IActivitePolitique | undefined;

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
    routingResolveService = TestBed.inject(ActivitePolitiqueRoutingResolveService);
    service = TestBed.inject(ActivitePolitiqueService);
    resultActivitePolitique = undefined;
  });

  describe('resolve', () => {
    it('should return IActivitePolitique returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivitePolitique = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultActivitePolitique).toEqual({ id: 123 });
    });

    it('should return new IActivitePolitique if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivitePolitique = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultActivitePolitique).toEqual(new ActivitePolitique());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ActivitePolitique })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivitePolitique = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultActivitePolitique).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
