jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDonSang, DonSang } from '../don-sang.model';
import { DonSangService } from '../service/don-sang.service';

import { DonSangRoutingResolveService } from './don-sang-routing-resolve.service';

describe('Service Tests', () => {
  describe('DonSang routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DonSangRoutingResolveService;
    let service: DonSangService;
    let resultDonSang: IDonSang | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DonSangRoutingResolveService);
      service = TestBed.inject(DonSangService);
      resultDonSang = undefined;
    });

    describe('resolve', () => {
      it('should return IDonSang returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonSang = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonSang).toEqual({ id: 123 });
      });

      it('should return new IDonSang if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonSang = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDonSang).toEqual(new DonSang());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DonSang })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonSang = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonSang).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
