jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDonneur, Donneur } from '../donneur.model';
import { DonneurService } from '../service/donneur.service';

import { DonneurRoutingResolveService } from './donneur-routing-resolve.service';

describe('Service Tests', () => {
  describe('Donneur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DonneurRoutingResolveService;
    let service: DonneurService;
    let resultDonneur: IDonneur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DonneurRoutingResolveService);
      service = TestBed.inject(DonneurService);
      resultDonneur = undefined;
    });

    describe('resolve', () => {
      it('should return IDonneur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonneur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonneur).toEqual({ id: 123 });
      });

      it('should return new IDonneur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonneur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDonneur).toEqual(new Donneur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Donneur })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonneur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonneur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
