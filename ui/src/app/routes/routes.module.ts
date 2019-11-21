import {NgModule} from '@angular/core';
import {LayoutComponent} from '../layout/layout.component';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '../shared/shared.module';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {path: 'dashboard', loadChildren: () => import('./dashboard/dashboard.module').then(mod => mod.DashboardModule)},
      {path: 'groups', loadChildren: () => import('./groups/groups.module').then(mod => mod.GroupsModule)},
      {path: 'projects', loadChildren: () => import('./projects/projects.module').then(mod => mod.ProjectsModule)},
      {path: 'settings', loadChildren: () => import('./settings/settings.module').then(mod => mod.SettingsModule)},
      {path: 'time-reports', loadChildren: () => import('./time-reports/time-reports.module').then(mod => mod.TimeReportsModule)},
    ],
    canActivate: [],
  },
  // {path: 'logout', component: UnauthorizedComponent, canActivate: [LogoutGuard]},
  {path: '**', redirectTo: 'dashboard'},
];

@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    RouterModule.forRoot(routes),
  ]
})
export class RoutesModule {
}
