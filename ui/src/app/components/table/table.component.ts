import {
  AfterViewChecked,
  ChangeDetectionStrategy,
  Component, ElementRef, EventEmitter,
  Input,
  OnChanges,
  Output,
  ViewChild
} from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import Project from '../../models/Project.model';
import SortData from '../../models/SortData.model';
import { TableService } from '../../services/table.service';
import { MatMenuTrigger } from '@angular/material/menu';
import { SELECTION_OPTIONS, STATUS_PRETTIER } from '../../constants';
import User from '../../models/User.model';
import { formatDate } from '@angular/common';
import Task from '../../models/Task.model';

@Component({
  selector: 'pts-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableComponent implements OnChanges, AfterViewChecked {

  public tableSource: MatTableDataSource<Project | Task>;
  public selection = new SelectionModel<Project | Task>(true, []);
  public tableColumns: string[];
  public sortData: SortData = {
    sortColumn: '',
    sortOrder: false
  };

  private expandedTableSource: MatTableDataSource<Project | Task>;
  private initialTableSource: MatTableDataSource<Project | Task>;

  @Input() private data: (Project | Task)[];
  @Input() public tableDataColumns: string[];
  @Input() public showNestedItems: boolean;
  @Input() public trigger: MatMenuTrigger;
  @Input() public allUsers: User[];

  @ViewChild('settingsElement', {static: false})
  settingsElement: ElementRef;

  @Output() settingsPosition = new EventEmitter();
  @Output() rowSelected = new EventEmitter();
  @Output() deleteRow = new EventEmitter();
  @Output() rowClicked = new EventEmitter();

  constructor(private tableService: TableService) {
  }

  ngOnChanges(change): void {
    /** Set tableSource when data is passed from the parent. */
    if (change.data && this.data) {
      if (this.isTaskIsTypeOfData()) {
        this.tableSource = this.initialTableSource = new MatTableDataSource<Project | Task>(this.getFlatTasksArray([...this.data]));
      } else {
        /** Save initial calculation not to repeat it */
        this.tableSource = this.initialTableSource = new MatTableDataSource<Project | Task>([...this.data]);
        /** Clear intermediate calculation if new data received */
        this.expandedTableSource = null;
      }
      this.selection.clear();
      this.emitSelectedRows();
    }

    /** Do the first calculation of the tableSource with nested elements */
    if (this.showNestedItems && !this.expandedTableSource) {
      this.expandedTableSource = new MatTableDataSource<Project | Task>(this.getFlatProjectsArray([...this.data]));
      this.toggleTableData();
    }

    /** Clear current selection and switch tableData */
    if (change.showNestedItems) {
      this.selection.clear();
      this.emitSelectedRows();
      this.toggleTableData();
    }

    /** Change table columns */
    if (change.tableDataColumns) {
      this.tableColumns = ['select', ...this.tableDataColumns, 'settings'];
    }
  }

  ngAfterViewChecked(): void {
    /** Emit new position of setting icon for ColumnSelectionComponent */
    if (this.settingsElement) {
      this.settingsPosition.emit(this.settingsElement.nativeElement.getBoundingClientRect());
    }
  }

  public isTaskIsTypeOfData(): boolean {
    if (!this.data.length) {
      return false;
    } else {
      const {type} = this.data[0];
      return type === 'TASK' || type === 'FEATURE';
    }
  }

  public isSubTaskIsTypeOfData(): boolean {
    if (!this.data.length) {
      return false;
    } else {
      return this.data[0].type === 'SUBTASK';
    }
  }

  /** Whether the number of selected elements matches the total number of rows. */
  public isAllSelected(): boolean {
    if (!this.tableSource.data.length) {
      return false;
    }
    const numSelected = this.selection.selected.length;
    const numRows = this.tableSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  public masterToggle(): void {
    this.isAllSelected() ? this.selection.clear() : this.tableSource.data.forEach(row => this.selection.select(row));
    this.emitSelectedRows();
  }

  /** Recursively traverse array to make it flat */
  private getFlatProjectsArray(arr: (Project | Task)[]): Project[] {
    const result = [];
    arr.forEach((item: Project, index: number) => {
      /** Set property "lastElement", it is necessary for layout */
      item.lastElement = arr.length - 1 === index;
      result.push(item);
      if (item.subprojects.length) {
        result.push(...this.getFlatProjectsArray(item.subprojects));
      }
    });
    return result;
  }

  /** Recursively traverse array to make it flat */
  private getFlatTasksArray(arr: (Project | Task)[]): Task[] {
    const result = [];
    arr.forEach((item: Task, index: number) => {
      /** Set property "lastElement", it is necessary for layout */
      item.lastElement = arr.length - 1 === index;
      result.push(item);
      if (item.subtasks.length) {
        result.push(...this.getFlatTasksArray(item.subtasks));
      }
    });
    return result;
  }

  /** Set tableData with expanded nested elements; otherwise with hidden nested elements. */
  private toggleTableData(): void {
    this.tableSource = this.showNestedItems ? this.expandedTableSource : this.initialTableSource;
  }

  public rowSelectionChanged(row): void {
    if (this.isTaskIsTypeOfData() || this.isSubTaskIsTypeOfData()) {
      this.taskRowSelectionChanged(row);
    } else {
      this.projectRowSelectionChanged(row);
    }
  }

  /** Select row */
  private projectRowSelectionChanged(row: Project): void {
    /** Switch the selection value to the opposite */
    this.selection.toggle(row);
    const checked = this.selection.isSelected(row);
    const hasSubProjects = row.subprojects.length;

    /** Switch the selection value of nested elements to the opposite by parent value */
    if (this.showNestedItems && hasSubProjects) {
      this.toggleSelectedValue(row.subprojects, checked);
    }

    /** Deselect parent element when one nested element is deselected */
    if (this.showNestedItems && row.parentId && !checked) {
      this.deselectParentElement(row.parentId);
    }

    /** Select parent element when all nested elements is selected */
    if (this.showNestedItems && row.parentId && checked) {
      this.selectParentElement(row.parentId);
    }

    /** Emit list of selected rows */
    this.emitSelectedRows();
  }

  /** Select row */
  private taskRowSelectionChanged(row: Task): void {
    console.log(row)
    /** Switch the selection value to the opposite */
    this.selection.toggle(row);
    const checked = this.selection.isSelected(row);

    /** Switch the selection value of nested elements to the opposite by parent value */
    if (row.subtasks.length) {
      this.toggleSelectedValue(row.subtasks, checked);
    }

    /** Deselect parent element when one nested element is deselected */
    if (!this.isSubTaskIsTypeOfData() && row.parentId && !checked) {
      this.deselectParentElement(row.parentId);
    }

    /** Select parent element when all nested elements is selected */
    if (!this.isSubTaskIsTypeOfData() && row.parentId && checked) {
      this.selectParentElement(row.parentId);
    }

    /** Emit list of selected rows */
    this.emitSelectedRows();
  }

  /** Select or deselect nested elements by parent value "checked" */
  private toggleSelectedValue(arr: (Project | Task)[], checked): void {
    arr.forEach((item: (Project | Task)) => {
      checked ? this.selection.select(item) : this.selection.deselect(item);
    });
  }

  /** Find parent element for deselected nested element, then deselect it */
  private deselectParentElement(id: number): void {
    const parentElement = this.selection.selected.find((item: (Project | Task)) => item.id === id);
    this.selection.deselect(parentElement);
  }

  /** Find all nested elements of parent element, check whether they all selected, and if true select parent element */
  private selectParentElement(id: number): void {
    const elements = this.tableSource.data.filter((item: (Project | Task)) => item.parentId === id);
    const parentElementShouldBeSelected = elements.every((item: (Project | Task)) => this.selection.isSelected(item));
    if (!parentElementShouldBeSelected) {
      return;
    }
    const parentElement = this.tableSource.data.find((item: (Project | Task)) => item.id === id);
    this.selection.select(parentElement);
  }

  // ToDo Rewrite this method later, options for filter should be received from server
  public getFilterOptions(column: string): string[] {
    const options = new Set(['All']);
    this.tableSource.data.forEach((item: Project) => options.add(item[column]));
    return [...options];
  }

  /** Set sort data(column and order). Sort order for ascend is "true", for descend is "false" */
  public sortTable(column: string): void {
    if (this.sortData.sortColumn !== column) {
      this.sortData.sortColumn = column;
      this.sortData.sortOrder = true;
    } else {
      this.sortData.sortOrder = !this.sortData.sortOrder;
    }

    /** Emit event for subscribers */
    this.tableService.tableSorted(this.sortData);
  }

  /** Toggle ColumnSelectedComponent */
  public onSettingsClick(): void {
    this.trigger.toggleMenu();
  }

  /** Navigate to new route */
  public selectRow(row: (Project | Task)): void {
    this.rowClicked.emit(row);
  }

  public trackByFn(index: number): number {
    return index;
  }

  public onDeleteIconClick(event, element): void {
    event.stopPropagation();
    this.deleteRow.emit(element);
  }

  public onMoveClick(event): void {
    event.stopPropagation();
    console.log(event);
  }

  private emitSelectedRows(): void {
    this.rowSelected.emit(this.selection.selected);
  }

  public getTextNode(column, element): string {
    const {status, projectLeadEmail, managerEmail, assigneeEmail} = element;
    if (column === 'status') {
      return STATUS_PRETTIER[status];
    } else if (column === 'projectLeadEmail' && projectLeadEmail) {
      return this.getUserByEmail(projectLeadEmail);
    } else if (column === 'managerEmail' && managerEmail) {
      return this.getUserByEmail(managerEmail);
    } else if (column === 'assigneeEmail' && assigneeEmail) {
      return this.getUserByEmail(assigneeEmail);
    } else if (column === 'changedDate' || column === 'deadline' || column === 'startDate') {
      return this.transformDateToFormat(element[column]);
    } else {
      return element[column];
    }
  }

  private getUserByEmail(data: string): string {
    if (this.allUsers.length) {
      const user = this.allUsers.find(({email}) => email === data);
      if (!user) {
        return data;
      }
      const {firstName, lastName} = user;
      if (!firstName && !lastName) {
        return data;
      }
      return `${firstName ? firstName : ''} ${lastName ? lastName : ''}`;
    }
  }

  public getTitleTextNode(column): string {
    return SELECTION_OPTIONS[column];
  }

  public isDateFilter(column): boolean {
    return column === 'changedDate' || column === 'startDate' || column === 'deadline';
  }

  public isOptionFilter(column): boolean {
    return column === 'projectLeadEmail' || column === 'customer'
      || column === 'status' || column === 'groupName'
      || column === 'managerEmail' || column === 'assigneeEmail'
      || column === 'priority';
  }

  public isContainFilter(column): boolean {
    return column === 'name' || column === 'code';
  }

  private transformDateToFormat(date: string): string {
    if (!date) {
      return '';
    }
    const dateArray = date.split(' ');
    const parseDate = new Date(dateArray[0] + 'T' + dateArray[1] + ':00');
    return formatDate(parseDate, 'mediumDate', 'en-US');
  }
}

