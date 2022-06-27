import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEnsegnant, Ensegnant } from '../ensegnant.model';
import { EnsegnantService } from '../service/ensegnant.service';

import { EnsegnantRoutingResolveService } from './ensegnant-routing-resolve.service';

describe('Ensegnant routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EnsegnantRoutingResolveService;
  let service: EnsegnantService;
  let resultEnsegnant: IEnsegnant | undefined;

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
    routingResolveService = TestBed.inject(EnsegnantRoutingResolveService);
    service = TestBed.inject(EnsegnantService);
    resultEnsegnant = undefined;
  });

  describe('resolve', () => {
    it('should return IEnsegnant returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsegnant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEnsegnant).toEqual({ id: 123 });
    });

    it('should return new IEnsegnant if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsegnant = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEnsegnant).toEqual(new Ensegnant());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ensegnant })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsegnant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEnsegnant).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
