import { Component } from '@angular/core';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  public isSidebarOpened = false;

  public onSidebarStatusChange(status): void {
    this.isSidebarOpened = status;
  }

}
