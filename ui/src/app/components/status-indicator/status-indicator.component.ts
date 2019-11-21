import { Component, Input} from '@angular/core';
import { STATUS_PRETTIER } from '../../constants';

@Component({
  selector: 'pts-status-indicator',
  templateUrl: './status-indicator.component.html',
  styleUrls: ['./status-indicator.component.scss']
})
export class StatusIndicatorComponent {

  @Input() status: string;
  public getStatus(): string {
    if (this.status) {
      return STATUS_PRETTIER[this.status];
    }
  }
}
