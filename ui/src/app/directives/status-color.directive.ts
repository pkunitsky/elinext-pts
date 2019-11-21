import { Directive, HostBinding, Input } from '@angular/core';
import { STATUS_COLORS } from '../constants';
import Project from '../models/Project.model';

@Directive({
  selector: '[ptsStatusColor]'
})
export class StatusColorDirective {
  @Input() private column: string;
  @Input() private element: Project;

  @HostBinding('style.color')
  get getStatusColor() {
    if (this.column !== 'status') {
      return;
    }
    return STATUS_COLORS[this.element.status];
  }
}
