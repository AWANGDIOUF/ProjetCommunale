jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICible, Cible } from '../cible.model';
import { CibleService } from '../service/cible.service';

import { CibleRoutingResolveService } from './cible-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cible routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CibleRoutingResolveService;
    let service: CibleService;
    let resultCible: ICible | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CibleRoutingResolveService);
      service = TestBed.inject(CibleService);
      resultCible = undefined;
    });

    describe('resolve', () => {
      it('should return ICible returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCible = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCible).toEqual({ id: 123 });
      });

      it('should return new ICible if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCible = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCible).toEqual(new Cible());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cible })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCible = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCible).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
