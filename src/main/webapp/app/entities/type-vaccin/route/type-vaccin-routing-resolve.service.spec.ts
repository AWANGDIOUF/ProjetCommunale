import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITypeVaccin, TypeVaccin } from '../type-vaccin.model';
import { TypeVaccinService } from '../service/type-vaccin.service';

import { TypeVaccinRoutingResolveService } from './type-vaccin-routing-resolve.service';

describe('TypeVaccin routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TypeVaccinRoutingResolveService;
  let service: TypeVaccinService;
  let resultTypeVaccin: ITypeVaccin | undefined;

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
    routingResolveService = TestBed.inject(TypeVaccinRoutingResolveService);
    service = TestBed.inject(TypeVaccinService);
    resultTypeVaccin = undefined;
  });

  describe('resolve', () => {
    it('should return ITypeVaccin returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeVaccin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeVaccin).toEqual({ id: 123 });
    });

    it('should return new ITypeVaccin if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeVaccin = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTypeVaccin).toEqual(new TypeVaccin());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeVaccin })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeVaccin = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeVaccin).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
