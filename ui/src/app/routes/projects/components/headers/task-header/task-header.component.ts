import { Component, Injector, Input } from '@angular/core';
import { BaseHeaderComponent } from '../base-header.component';
import Project from '../../../../../models/Project.model';
import Task from '../../../../../models/Task.model';
import PageTimeReport from '../../../../../models/PageTimeReport.model';

@Component({
  selector: 'pts-task-header',
  templateUrl: './task-header.component.html',
  styleUrls: ['./task-header.component.scss']
})
export class TaskHeaderComponent  extends BaseHeaderComponent {
  @Input() public project: Project;
  @Input() public subProject: Project;
  @Input() public feature: Task;
  @Input() public pageReport: PageTimeReport;
  @Input() public instance: Task;

  constructor(public injector: Injector) {
    super(injector);
  }

  public onAddReport(): void {
    this.onAdd({id: this.instance.id});
  }
}
