import { Component, Injector, Input } from '@angular/core';
import { BaseHeaderComponent } from '../base-header.component';
import Project from '../../../../../models/Project.model';
import CreateEditData from '../../../../../models/CreateEditData.model';
import { CREATE_TASK } from '../../../../../constants';

@Component({
  selector: 'pts-subproject-header',
  templateUrl: './subproject-header.component.html',
  styleUrls: ['../base-header.component.scss']
})
export class SubprojectHeaderComponent extends BaseHeaderComponent {

  @Input() public instance: Project;

  constructor(public injector: Injector) {
    super(injector);
  }

  onCreateTaskClick(): void {
    const createData: CreateEditData = {type: CREATE_TASK, id: this.instance.id};
    this.onAdd(createData);
  }
}
