import { Directive, HostBinding, Input } from '@angular/core';
import { STATUS_COLORS } from '../constants';

@Directive({
  selector: '[ptsHeaderStatusColor]'
})
export class HeaderStatusColorDirective {
  @Input() private status: string;

  @HostBinding('style.background')
  get getBackgroundColor() {
    return STATUS_COLORS[this.status];
  }
}
