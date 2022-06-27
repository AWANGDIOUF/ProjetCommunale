import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IArchiveSport, ArchiveSport } from '../archive-sport.model';
import { ArchiveSportService } from '../service/archive-sport.service';

import { ArchiveSportRoutingResolveService } from './archive-sport-routing-resolve.service';

describe('ArchiveSport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ArchiveSportRoutingResolveService;
  let service: ArchiveSportService;
  let resultArchiveSport: IArchiveSport | undefined;

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
    routingResolveService = TestBed.inject(ArchiveSportRoutingResolveService);
    service = TestBed.inject(ArchiveSportService);
    resultArchiveSport = undefined;
  });

  describe('resolve', () => {
    it('should return IArchiveSport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArchiveSport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArchiveSport).toEqual({ id: 123 });
    });

    it('should return new IArchiveSport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArchiveSport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultArchiveSport).toEqual(new ArchiveSport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ArchiveSport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArchiveSport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArchiveSport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
