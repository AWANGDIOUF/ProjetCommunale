import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeInterview, DemandeInterview } from '../demande-interview.model';
import { DemandeInterviewService } from '../service/demande-interview.service';

import { DemandeInterviewRoutingResolveService } from './demande-interview-routing-resolve.service';

describe('DemandeInterview routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeInterviewRoutingResolveService;
  let service: DemandeInterviewService;
  let resultDemandeInterview: IDemandeInterview | undefined;

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
    routingResolveService = TestBed.inject(DemandeInterviewRoutingResolveService);
    service = TestBed.inject(DemandeInterviewService);
    resultDemandeInterview = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeInterview returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInterview = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInterview).toEqual({ id: 123 });
    });

    it('should return new IDemandeInterview if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInterview = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeInterview).toEqual(new DemandeInterview());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeInterview })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInterview = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInterview).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
