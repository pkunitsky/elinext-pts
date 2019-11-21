import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MaterialUiModule } from '../material-ui/material-ui.module';
import { StatusIndicatorComponent } from '../components/status-indicator/status-indicator.component';
import { HeaderStatusColorDirective } from '../directives/header-status-color.directive';
import { AdditionalInfoComponent } from '../components/additional-info/additional-info.component';
import { ButtonComponent } from '../components/button/button.component';


@NgModule({
  declarations: [
    StatusIndicatorComponent,
    HeaderStatusColorDirective,
    AdditionalInfoComponent,
    ButtonComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MaterialUiModule,
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MaterialUiModule,
    StatusIndicatorComponent,
    HeaderStatusColorDirective,
    AdditionalInfoComponent,
    ButtonComponent
  ]
})
export class SharedModule {
}
