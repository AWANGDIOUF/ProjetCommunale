jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { BeneficiaireService } from '../service/beneficiaire.service';

import { BeneficiaireRoutingResolveService } from './beneficiaire-routing-resolve.service';

describe('Service Tests', () => {
  describe('Beneficiaire routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BeneficiaireRoutingResolveService;
    let service: BeneficiaireService;
    let resultBeneficiaire: IBeneficiaire | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BeneficiaireRoutingResolveService);
      service = TestBed.inject(BeneficiaireService);
      resultBeneficiaire = undefined;
    });

    describe('resolve', () => {
      it('should return IBeneficiaire returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBeneficiaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBeneficiaire).toEqual({ id: 123 });
      });

      it('should return new IBeneficiaire if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBeneficiaire = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBeneficiaire).toEqual(new Beneficiaire());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Beneficiaire })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBeneficiaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBeneficiaire).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
