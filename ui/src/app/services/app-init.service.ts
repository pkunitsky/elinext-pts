import {Injectable} from '@angular/core';
import {AuthService} from './auth/auth.service';

@Injectable()
export class AppInitService {

  constructor(private authService: AuthService) {
  }

  public init(): Promise<any> {
    console.debug('[APP-INIT-SERVICE]');
    return this.authService.loadOpenIdConfigurationAndTryLogin().then(() => {
      if (this.authService.isAuthorized()) {
        // Subscribing to async requests
        // this.launchAsyncRequests().subscribe();
        // Return sync requests
        // return this.loadSyncRequests();
        return this.authService.loadUserProfile();
      } else {
        return new Promise((resolve, reject) => resolve());
      }
    });
  }

  // private loadSyncRequests(): Promise<any> {
  //   return Promise.all([
  //     this.loadStaticBundle(),
  //     this.loadSettings(),
  //     this.loadUserProfile(),
  //     this.loadGridSettings()
  //   ]);
  // }
  //
  // private launchAsyncRequests(): Observable<any> {
  //   return forkJoin([
  //     this.loadTasks(),
  //     this.loadActivitiesCount(),
  //     this.loadMessages(),
  //     this.connectSignalR()
  //   ]);
  // }
}
