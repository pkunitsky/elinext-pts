import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges, SimpleChanges
} from '@angular/core';
import Project from '../../../../models/Project.model';
import Task from '../../../../models/Task.model';

@Component({
  selector: 'pts-title',
  templateUrl: './title.component.html',
  styleUrls: ['./title.component.scss']
})
export class TitleComponent implements OnChanges {
  public nesting: number;

  @Input() public project: Project;
  @Input() public subProject: Project;
  @Input() public feature: Task;
  @Input() public task: Task;

  @Output() public toBack = new EventEmitter();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.project.currentValue) {
      this.nesting = [this.project, this.subProject, this.feature, this.task].filter(item => item).length;
    }
  }

  public onBackArrowClick(): void {
    this.toBack.emit();
  }

  public getCssClass(): string {
    if (this.nesting) {
      return 'title-wrapper-' + this.nesting;
    }
  }
}
