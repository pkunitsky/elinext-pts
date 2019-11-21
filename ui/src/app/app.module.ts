import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {SharedModule} from './shared/shared.module';
import {RoutesModule} from './routes/routes.module';
import {LayoutModule} from './layout/layout.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialUiModule} from './material-ui/material-ui.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {DataService} from './services/data.service';
import {NavigationService} from './services/navigation.service';

import {FormsModule} from '@angular/forms';
import {OAuthModule, OAuthStorage} from 'angular-oauth2-oidc';
import {AppInitService} from './services/app-init.service';
import {AuthInterceptor} from './services/auth/auth.interceptor';
import {AuthService} from './services/auth/auth.service';

export function loadConfigAndInitAuth(appInitService: AppInitService): Function {
  return (): Promise<any> => appInitService.init();
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    RoutesModule,
    LayoutModule,
    SharedModule,
    BrowserAnimationsModule,
    MaterialUiModule,
    HttpClientModule,
    FormsModule,
    OAuthModule.forRoot(),
  ],
  providers: [
    AppInitService,
    AuthService,
    {
      // Load the server settings and get the capabilities of the authentication server
      provide: APP_INITIALIZER,
      useFactory: loadConfigAndInitAuth,
      deps: [AppInitService],
      multi: true
    },
    {provide: OAuthStorage, useValue: localStorage},
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    DataService,
    NavigationService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
