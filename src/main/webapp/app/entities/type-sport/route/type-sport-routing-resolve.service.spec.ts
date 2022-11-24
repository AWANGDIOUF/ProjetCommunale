jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITypeSport, TypeSport } from '../type-sport.model';
import { TypeSportService } from '../service/type-sport.service';

import { TypeSportRoutingResolveService } from './type-sport-routing-resolve.service';

describe('Service Tests', () => {
  describe('TypeSport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TypeSportRoutingResolveService;
    let service: TypeSportService;
    let resultTypeSport: ITypeSport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TypeSportRoutingResolveService);
      service = TestBed.inject(TypeSportService);
      resultTypeSport = undefined;
    });

    describe('resolve', () => {
      it('should return ITypeSport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeSport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeSport).toEqual({ id: 123 });
      });

      it('should return new ITypeSport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeSport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTypeSport).toEqual(new TypeSport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeSport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeSport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeSport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
