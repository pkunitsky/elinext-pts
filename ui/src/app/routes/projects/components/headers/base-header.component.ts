import { Component, EventEmitter, Injector, Input, Output } from '@angular/core';
import Project from '../../../../models/Project.model';
import Task from '../../../../models/Task.model';
import { TableService } from '../../../../services/table.service';
import { NavigationService } from '../../../../services/navigation.service';
import User from '../../../../models/User.model';
import ArchiveData from '../../../../models/ArchiveData.model';
import { CHILD_ELEMENT, INFO_NAMES, PROJECT_WITH_TASKS_ROUTE, ROOT_ELEMENT } from '../../../../constants';
import CreateEditData from '../../../../models/CreateEditData.model';

@Component({
  selector: 'pts-base-header',
  template: '',
})
export class BaseHeaderComponent {

  protected tableService: TableService;
  protected navigationService: NavigationService;

  public showSubProjects = false;
  public isDescriptionShown = true;

  @Input() public selected: (Project | Task)[] = [];
  @Input() public instance: (Project | Task);
  @Input() public users: User[] = [];
  @Input() public routeType: string;
  @Input() public parentInstance: (Project | Task);
  @Input() public grandparentInstance: Project;

  @Output() public move = new EventEmitter();
  @Output() public archive = new EventEmitter();
  @Output() public edit = new EventEmitter();
  @Output() public add = new EventEmitter();
  @Output() public changesLog = new EventEmitter();
  @Output() public join = new EventEmitter();
  @Output() public joinInFeature = new EventEmitter();

  constructor(public injector: Injector) {
    this.tableService = injector.get(TableService);
    this.navigationService = injector.get(NavigationService);
  }

  /** Navigate to parent route when back arrow was clicked */
  public onBackArrowClick(): void {
    this.navigationService.navigateStepBack();
  }

  public onMoveClick(): void {
    this.move.emit(this.instance);
  }

  /** Archive parent element */
  public onArchiveClick(): void {
    const archiveData: ArchiveData = {
      ids: [this.instance.id], type: ROOT_ELEMENT, instance: this.instance.type
    };
    this.archive.emit(archiveData);
  }

  /** Archive children selected elements */
  public onArchiveSelectedClick(): void {
    const archiveData: ArchiveData = {
      ids: this.selected.map(item => item.id), type: CHILD_ELEMENT, instance: this.selected[0].type
    };
    this.archive.emit(archiveData);
  }

  public onEditClick(): void {
    this.edit.emit(this.instance);
  }

  public onAdd(data: CreateEditData): void {
    this.add.emit(data);
  }

  public onChangeLogClick(): void {
    this.changesLog.emit(this.instance.id);
  }

  public toggleDescription(): void {
    this.isDescriptionShown = !this.isDescriptionShown;
  }

  public getAdditionalInfoItems(): string[] {
    if (this.instance) {
      return Object.keys(this.instance).filter(item => INFO_NAMES.includes(item));
    }
  }

  public subProjectsToggle(): void {
    this.showSubProjects = !this.showSubProjects;
    /** Emit event of changing subProjects visibility */
    this.tableService.toggleNestedItems(this.showSubProjects);
  }

  /** Trigger join project event */
  public onJoinCheckedClick(): void {
    this.join.emit(true);
  }

  public onJoinInFeature(): void {
    const ids = this.selected.map(item => item.id);
    this.joinInFeature.emit(ids);
  }

  public isProjectTasksRoute(): boolean {
    return this.routeType === PROJECT_WITH_TASKS_ROUTE;
  }
}
