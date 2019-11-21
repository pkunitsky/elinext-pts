import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'pts-mobile-header',
  templateUrl: './mobile-header.component.html',
  styleUrls: ['./mobile-header.component.scss']
})
export class MobileHeaderComponent {
  public isSidebarOpen = false;
  @Output() public sidebarStatus = new EventEmitter();

  public toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.sidebarStatus.emit(this.isSidebarOpen);
  }

}
