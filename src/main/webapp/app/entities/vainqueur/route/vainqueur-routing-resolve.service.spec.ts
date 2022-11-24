jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVainqueur, Vainqueur } from '../vainqueur.model';
import { VainqueurService } from '../service/vainqueur.service';

import { VainqueurRoutingResolveService } from './vainqueur-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vainqueur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VainqueurRoutingResolveService;
    let service: VainqueurService;
    let resultVainqueur: IVainqueur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VainqueurRoutingResolveService);
      service = TestBed.inject(VainqueurService);
      resultVainqueur = undefined;
    });

    describe('resolve', () => {
      it('should return IVainqueur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVainqueur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVainqueur).toEqual({ id: 123 });
      });

      it('should return new IVainqueur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVainqueur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVainqueur).toEqual(new Vainqueur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vainqueur })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVainqueur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVainqueur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
