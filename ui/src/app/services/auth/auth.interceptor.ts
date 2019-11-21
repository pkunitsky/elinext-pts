import {Injectable, Injector} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {tap} from 'rxjs/internal/operators';
import {OAuthService} from 'angular-oauth2-oidc';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private injector: Injector,
              private router: Router) {
  }

  public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let requestToForward = req;
    const oAuthService: OAuthService = this.injector.get(OAuthService);
    if (oAuthService.hasValidAccessToken() && !req.url.includes('.well-known')) {
      requestToForward = req.clone({setHeaders: {'Authorization': oAuthService.authorizationHeader()}});
    }
    return next.handle(requestToForward).pipe(tap(resp => {
    }, error => {
      if (error instanceof HttpErrorResponse) {
        if (error.status === 401) {
          oAuthService.initImplicitFlow();
        }
        if (error.status >= 500) {
          // this.toasterMessagesService.showCommonErrorMessage();
        }
      }
    }));

  }
}
