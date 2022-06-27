import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICalendrierEvenement, CalendrierEvenement } from '../calendrier-evenement.model';
import { CalendrierEvenementService } from '../service/calendrier-evenement.service';

import { CalendrierEvenementRoutingResolveService } from './calendrier-evenement-routing-resolve.service';

describe('CalendrierEvenement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CalendrierEvenementRoutingResolveService;
  let service: CalendrierEvenementService;
  let resultCalendrierEvenement: ICalendrierEvenement | undefined;

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
    routingResolveService = TestBed.inject(CalendrierEvenementRoutingResolveService);
    service = TestBed.inject(CalendrierEvenementService);
    resultCalendrierEvenement = undefined;
  });

  describe('resolve', () => {
    it('should return ICalendrierEvenement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCalendrierEvenement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCalendrierEvenement).toEqual({ id: 123 });
    });

    it('should return new ICalendrierEvenement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCalendrierEvenement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCalendrierEvenement).toEqual(new CalendrierEvenement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CalendrierEvenement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCalendrierEvenement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCalendrierEvenement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
