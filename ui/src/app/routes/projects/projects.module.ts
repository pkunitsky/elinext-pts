import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProjectsRoutingModule } from './projects-routing.module';

import { TableComponent } from '../../components/table/table.component';
import { ColumnSelectionComponent } from '../../components/column-selection/column-selection.component';
import { PaginatorComponent } from '../../components/paginator/paginator.component';
import { RootHeaderComponent } from './components/headers/root-header/root-header.component';
import { ArchiveJoinComponent } from '../../components/modals/archive-join/archive-join.component';
import { CreateEditComponent } from '../../components/modals/create-edit/create-edit.component';
import { ProjectHeaderComponent } from './components/headers/project-header/project-header.component';
import { DeleteComponent } from '../../components/modals/delete/delete.component';
import { ChangesLogComponent } from '../../components/modals/changes-log/changes-log.component';
import { CreateFeatureComponent } from '../../components/modals/create-feature/create-feature.component';
import { CreateEditReportComponent } from '../../components/modals/create-edit-report/create-edit-report.component';
import { BaseHeaderComponent } from './components/headers/base-header.component';
import { BaseListComponent } from './components/base-list.component';
import { RootListComponent } from './components/root-list/root-list.component';
import { ProjectListComponent } from './components/project-list/project-list.component';

import { SubprojectHeaderComponent } from './components/headers/subproject-header/subproject-header.component';
import { SubprojectListComponent } from './components/subproject-list/subproject-list.component';
import { TitleComponent } from './components/title/title.component';
import { TaskHeaderComponent } from './components/headers/task-header/task-header.component';
import { FeatureHeaderComponent } from './components/headers/feature-header/feature-header.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { FeatureListComponent } from './components/feature-list/feature-list.component';

import { TableService } from '../../services/table.service';

import { FloatLabelDirective } from '../../directives/float-label.directive';
import { TableSortColumnDirective } from '../../directives/table-sort-column.directive';
import { RowHeightDirective } from '../../directives/row-height.directive';
import { NestedRowDirective } from '../../directives/nested-row.directive';
import { SettingsIconDirective } from '../../directives/settings-icon.directive';
import { TooltipDirective } from '../../directives/tooltip.directive';
import { StatusColorDirective } from '../../directives/status-color.directive';
import { ChangesLogLineDirective } from '../../directives/changes-log-line.directive';
import { ProgressDirective } from '../../directives/progress.directive';

import { FirstLetterToUpperCasePipe } from '../../pipes/first-letter-to-upper-case.pipe';

@NgModule({
  declarations: [
    TableComponent,
    TableSortColumnDirective,
    RowHeightDirective,
    NestedRowDirective,
    ColumnSelectionComponent,
    SettingsIconDirective,
    PaginatorComponent,
    RootHeaderComponent,
    TooltipDirective,
    ArchiveJoinComponent,
    CreateEditComponent,
    FloatLabelDirective,
    ProjectHeaderComponent,
    DeleteComponent,
    ChangesLogComponent,
    StatusColorDirective,
    ChangesLogLineDirective,
    FirstLetterToUpperCasePipe,
    CreateFeatureComponent,
    CreateEditReportComponent,
    ProgressDirective,
    BaseHeaderComponent,
    BaseListComponent,
    RootListComponent,
    ProjectListComponent,
    ProjectHeaderComponent,
    SubprojectHeaderComponent,
    SubprojectListComponent,
    TitleComponent,
    TaskHeaderComponent,
    FeatureHeaderComponent,
    TaskListComponent,
    FeatureListComponent,
  ],
  imports: [
    ProjectsRoutingModule,
    SharedModule
  ],
  providers: [TableService],
  entryComponents: [
    ArchiveJoinComponent,
    CreateEditComponent,
    DeleteComponent,
    ChangesLogComponent,
    CreateFeatureComponent,
    CreateEditReportComponent
  ],
})
export class ProjectsModule {
}
