import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NavigationService } from '../../services/navigation.service';

@Component({
  selector: 'pts-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  public isSidebarOpen = false;
  public url: string;
  @Output() public sidebarStatus = new EventEmitter();

  constructor(private navigation: NavigationService) { }

  ngOnInit() {
    this.url = '/' + this.navigation.getCurrentUrl().split('/')[1];
  }

  public toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.sidebarStatus.emit(this.isSidebarOpen);
  }

  public getUserName(): string {
    // ToDo Rewrite, return valid value
    return 'John Smith';
  }

  public getUserEmail(): string {
    // ToDo Rewrite, return valid value
    return 'john.smith@elilink.com';
  }

  public getUserNameAbbreviation(): string {
    // ToDo Rewrite, return valid value
    return 'JS';
  }

  public onSearchIconClick(): void {
    this.toggleSidebar();
  }

  public navigateTo(route: string): void {
    this.navigation.navigateToRoute(route);
    this.url = route;
  }
}
