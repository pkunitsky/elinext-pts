import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { LayoutComponent } from './layout.component';
import { MobileHeaderComponent } from './mobile-header/mobile-header.component';


@NgModule({
  declarations: [
    SidebarComponent,
    LayoutComponent,
    MobileHeaderComponent
  ],
  imports: [
    SharedModule
  ]
})
export class LayoutModule {
}
