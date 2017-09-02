import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RoutesService {

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
  }

  public actualCurrentRouteData(): Observable<any> {
    return this.actualCurrentRoute().mergeMap((route) => route.data);
  }

  public actualCurrentRoute(): Observable<any> {
    // it the subscribing component is not the root component directly invoked by the router,
    // the route passed by activatedRoute is the root route object, so we need to navigate to the bottom
    // to get the actual params
    return this.router.events
      .filter((event) => event instanceof NavigationEnd)
      .map(() => this.activatedRoute)
      .map((route) => {
        while (route.firstChild) {
          route = route.firstChild;
        }
        return route;
      });
  }
}
