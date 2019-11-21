import { Component, Injector, Input } from '@angular/core';
import { BaseHeaderComponent } from '../base-header.component';
import CreateEditData from '../../../../../models/CreateEditData.model';
import Project from '../../../../../models/Project.model';
import {
  CREATE_SUBPROJECT,
  CREATE_TASK,
  PROJECT_ROUTE,
  PROJECT_WITH_TASKS_ROUTE
} from '../../../../../constants';

@Component({
  selector: 'pts-project-info-header',
  templateUrl: './project-header.component.html',
  styleUrls: ['../base-header.component.scss']
})
export class ProjectHeaderComponent extends BaseHeaderComponent {

  @Input() public instance: Project;

  constructor(public injector: Injector) {
    super(injector);
  }

  public getSectionName(): string {
    if (this.instance) {
      switch (this.routeType) {
        case PROJECT_ROUTE:
          return this.instance.subprojects.length ? 'Subprojects' : 'Subprojects or Tasks';
        case PROJECT_WITH_TASKS_ROUTE:
          return 'Tasks';
      }
    }
  }

  public isCreateTaskVisible(): boolean {
    if (this.instance) {
      return this.isProjectTasksRoute() || !this.isProjectTasksRoute() && !this.instance.subprojects.length;
    }
  }

  public onCreateTaskClick(): void {
    const createData: CreateEditData = {type: CREATE_TASK, id: this.instance.id};
    this.onAdd(createData);
  }

  public onCreateSubProjectClick(): void {
    const createData: CreateEditData = {type: CREATE_SUBPROJECT, id: this.instance.id};
    this.onAdd(createData);
  }
}
