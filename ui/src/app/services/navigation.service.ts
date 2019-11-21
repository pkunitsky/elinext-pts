import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(
    private router: Router
  ) { }

  public navigateToRouteBySelectedRow(data): void {
    const {type, id, subtasks, parentId} = data;
    const url = this.getCurrentUrl();
    const isFeatureRoute = url.split('/').includes('feature');
    let route = '';

    switch (type) {
      case 'PROJECT':
        route = `${url}/${id}`;
        break;
      case 'SUBPROJECT':
        route = url.split('/').length === 2 ? `${url}/${parentId}/subproject/${id}` : `${url}/subproject/${id}`;
        break;
      case 'TASK':
        route = subtasks.length ? `${url}/feature/${id}` : `${url}/task/${id}`;
        break;
      case 'SUBTASK':
        route = isFeatureRoute ? `${url}/task/${id}` : `${url}/feature/${parentId}/task/${id}`;
    }

    this.navigateToRoute(route);
  }

  public navigateStepBack(): void {
    const currentUrl = this.getCurrentUrl().split('/');
    let route = '';
    if (currentUrl.length === 3 && currentUrl[1] === 'projects') {
      route = `/projects`;
    } else {
      route = currentUrl.slice(0, -2).join('/');
    }
    this.navigateToRoute(route);
  }

  public navigateToRoute(route: string): void {
    this.router.navigate([route]);
  }

  public getCurrentUrl(): string {
    return this.router.url;
  }
}
