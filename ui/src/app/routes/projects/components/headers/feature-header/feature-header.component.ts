import { Component, Injector, Input } from '@angular/core';
import { BaseHeaderComponent } from '../base-header.component';
import Task from '../../../../../models/Task.model';
import CreateEditData from '../../../../../models/CreateEditData.model';
import { CREATE_SUBTASK } from '../../../../../constants';

@Component({
  selector: 'pts-feature-header',
  templateUrl: './feature-header.component.html',
  styleUrls: ['../base-header.component.scss']
})
export class FeatureHeaderComponent extends BaseHeaderComponent {
  @Input() public instance: Task;

  constructor(public injector: Injector) {
    super(injector);
  }

  public onCreateTaskClick(): void {
    const createData: CreateEditData = {type: CREATE_SUBTASK, id: this.instance.id};
    this.onAdd(createData);
  }
}
