import {Injectable, Injector} from '@angular/core';
import {AuthConfig, JwksValidationHandler, OAuthService} from 'angular-oauth2-oidc';
import {Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {AuthUserModel} from '../../models/auth-user.model';

@Injectable()
export class AuthService {
  private user: AuthUserModel;

  constructor(private oAuthService: OAuthService,
              private injector: Injector) {
  }

  public login(): void {
    this.oAuthService.initImplicitFlow();
  }

  public logout(): void {
    this.oAuthService.logOut();
  }

  public isAuthorized(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  public getToken(): string {
    return this.oAuthService.getAccessToken();
  }

  public getBearerHeader(): string {
    return this.oAuthService.authorizationHeader();
  }

  // public isUserAdmin(): boolean {
  //   const userRole: string = this.settingsService.getUserSetting('role');
  //   return userRole === ('Apps Admins' || 'Tracer Admins');
  // }

  public setRedirectUrl(): void {
    const windowUrl: string = window.location.href;
    const router: Router = this.injector.get(Router);
    let routerUrl: string;
    router.config.find(c => !c.path).children.filter(c => c.path).map(c => c.path)
      .forEach(p => {
        const number = window.location.href.indexOf(p);
        if (number > 0) {
          routerUrl = `/${windowUrl.substring(number)}`
        }
      });
    routerUrl && localStorage.setItem('redirectUrl', routerUrl);
  }

  public removeRedirectUrl(): void {
    localStorage.removeItem('redirectUrl');
  }

  public getRedirectUrl(): string {
    return localStorage.getItem('redirectUrl');
  }

  public loadUserProfile(): Promise<any> {
    return this.oAuthService.loadUserProfile().then((userData: any) => {
      this.user = {
        role: userData.role,
        email: userData.email
      }
    });
  }

  public loadOpenIdConfigurationAndTryLogin(): Promise<any> {
    console.debug('[CONFIG] oidcConfigService.load_using_stsServer');
    !this.getRedirectUrl() && this.setRedirectUrl();
    this.oAuthService.configure(this.getAuthConfig());
    this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    // this.oAuthService.setupAutomaticSilentRefresh();
    return this.oAuthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (!this.isAuthorized()) {
        this.login();
      }
    });
  }

  private getAuthConfig(): AuthConfig {
    // ToDo change params
    const authConfig: AuthConfig = {
      clientId: 'ElinextPts',
      scope: 'openid email',
      issuer: environment.authUrl,
      redirectUri: environment.ngAppUrl + '/callback',
      // ToDo delete it after all
      requireHttps: false
      // silentRefreshRedirectUri: config.ngAppServer + '/silent-refresh.html'
    };
    return authConfig;
  }


}
