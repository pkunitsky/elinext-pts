import { Component, Input, Output, EventEmitter } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { SELECTION_OPTIONS } from '../../constants';

@Component({
  selector: 'pts-column-selection',
  templateUrl: './column-selection.component.html',
  styleUrls: ['./column-selection.component.scss']
})
export class ColumnSelectionComponent {
  @Input() public options: string[];
  @Input() public selection: SelectionModel<string>;
  @Output() selectionChanged = new EventEmitter();

  public onSelectionChange(option): void {
    /** Do not remove last column */
    if (this.selection.selected.length < 2 && this.selection.isSelected(option)) {
      return;
    }
    this.selection.toggle(option);
    /** Emit event when columns were changed */
    this.selectionChanged.emit(this.selection);
  }

  /** Set "disabled" class to last option, set active class */
  public setCssClass(option): string {
    if (this.selection.selected.length < 2 && this.selection.isSelected(option)) {
      return 'option option-disabled';
    } else if (this.selection.isSelected(option)) {
      return 'option option-selected';
    } else {
      return 'option option-unchecked';
    }
  }

  public getOption(option): string {
    return SELECTION_OPTIONS[option];
  }
}
