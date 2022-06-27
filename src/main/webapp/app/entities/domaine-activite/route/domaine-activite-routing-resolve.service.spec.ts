import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDomaineActivite, DomaineActivite } from '../domaine-activite.model';
import { DomaineActiviteService } from '../service/domaine-activite.service';

import { DomaineActiviteRoutingResolveService } from './domaine-activite-routing-resolve.service';

describe('DomaineActivite routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DomaineActiviteRoutingResolveService;
  let service: DomaineActiviteService;
  let resultDomaineActivite: IDomaineActivite | undefined;

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
    routingResolveService = TestBed.inject(DomaineActiviteRoutingResolveService);
    service = TestBed.inject(DomaineActiviteService);
    resultDomaineActivite = undefined;
  });

  describe('resolve', () => {
    it('should return IDomaineActivite returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomaineActivite = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomaineActivite).toEqual({ id: 123 });
    });

    it('should return new IDomaineActivite if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomaineActivite = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDomaineActivite).toEqual(new DomaineActivite());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DomaineActivite })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomaineActivite = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomaineActivite).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
