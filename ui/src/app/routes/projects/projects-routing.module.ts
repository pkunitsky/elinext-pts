import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RootListComponent } from './components/root-list/root-list.component';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { SubprojectListComponent } from './components/subproject-list/subproject-list.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import { FeatureListComponent } from './components/feature-list/feature-list.component';

const routes: Routes = [
  {path: '', component: RootListComponent, pathMatch: 'full'},
  {path: ':projectId', component: ProjectListComponent, pathMatch: 'full'},
  {path: ':projectId/subproject/:subprojectId', component: SubprojectListComponent, pathMatch: 'full'},
  {path: ':projectId/feature/:featureId', component: FeatureListComponent, pathMatch: 'full'},
  {path: ':projectId/subproject/:subprojectId/feature/:featureId', component: FeatureListComponent, pathMatch: 'full'},
  {path: ':projectId/task/:taskId', component: TaskListComponent, pathMatch: 'full'},
  {path: ':projectId/feature/:featureId/task/:taskId', component: TaskListComponent, pathMatch: 'full'},
  {path: ':projectId/subproject/:subprojectId/task/:taskId', component: TaskListComponent, pathMatch: 'full'},
  {path: ':projectId/subproject/:subprojectId/feature/:featureId/task/:taskId', component: TaskListComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [RouterModule]
})
export class ProjectsRoutingModule {
}
