jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVaccination, Vaccination } from '../vaccination.model';
import { VaccinationService } from '../service/vaccination.service';

import { VaccinationRoutingResolveService } from './vaccination-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vaccination routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VaccinationRoutingResolveService;
    let service: VaccinationService;
    let resultVaccination: IVaccination | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VaccinationRoutingResolveService);
      service = TestBed.inject(VaccinationService);
      resultVaccination = undefined;
    });

    describe('resolve', () => {
      it('should return IVaccination returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaccination = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVaccination).toEqual({ id: 123 });
      });

      it('should return new IVaccination if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaccination = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVaccination).toEqual(new Vaccination());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vaccination })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVaccination = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVaccination).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
