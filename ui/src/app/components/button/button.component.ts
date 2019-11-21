import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'pts-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() public basicButton: boolean;
  @Input() public label: string;
  @Input() public width: number;

  @Output() public clickEvent = new EventEmitter();

  public onClick(): void {
    this.clickEvent.emit(true);
  }
}
