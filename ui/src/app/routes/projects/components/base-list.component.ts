import {
  AfterViewChecked,
  ChangeDetectorRef,
  Component,
  ElementRef, HostListener,
  Injector,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { DataService } from '../../../services/data.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatMenuTrigger } from '@angular/material';
import { NavigationService } from '../../../services/navigation.service';
import { TableService } from '../../../services/table.service';
import { of, Subscription, zip } from 'rxjs';
import User from '../../../models/User.model';
import { SelectionModel } from '@angular/cdk/collections';
import {
  CREATE_FEATURE,
  CREATE_SUBPROJECT, CREATE_SUBTASK, CREATE_TASK, EDIT_SUBPROJECT,
  INITIAL_PAGE, INITIAL_PROJECTS_TABLE_COLUMNS_NAME, INITIAL_TASKS_TABLE_COLUMNS_NAME,
  MOBILE_HEADER_HEIGHT,
  MOBILE_MAX_WIDTH, PROJECT_ROUTE, PROJECT_WITH_TASKS_ROUTE, PROJECTS_TABLE_COLUMNS_NAME, ROOT_ELEMENT,
  TABLET_MAX_WIDTH, TASKS_TABLE_COLUMNS_NAME
} from '../../../constants';
import PageProject from '../../../models/PageProject.model';
import Project from '../../../models/Project.model';
import Task from '../../../models/Task.model';
import { DeleteComponent } from '../../../components/modals/delete/delete.component';
import { ArchiveJoinComponent } from '../../../components/modals/archive-join/archive-join.component';
import ArchiveData from '../../../models/ArchiveData.model';
import { map } from 'rxjs/operators';
import { CreateEditComponent } from '../../../components/modals/create-edit/create-edit.component';
import PageTask from '../../../models/PageTask.model';
import { CreateFeatureComponent } from '../../../components/modals/create-feature/create-feature.component';
import PageChangeLog from '../../../models/PageChangeLog.model';
import { ChangesLogComponent } from '../../../components/modals/changes-log/changes-log.component';

@Component({
  selector: 'pts-base-list',
  template: '',
})
export class BaseListComponent implements OnInit, OnDestroy, AfterViewChecked {

  protected tableService: TableService;
  protected dataService: DataService;
  protected activatedRoute: ActivatedRoute;
  protected dialog: MatDialog;
  protected cdr: ChangeDetectorRef;
  protected navigationService: NavigationService;

  public subscription: Subscription = new Subscription();
  public showSubProjects: boolean;
  public users: User[] = [];
  public columns: string[] = [];
  public tableDataColumns: string[] = [];
  public columnSelected = new SelectionModel(true, []);
  public currentPage: number;
  public data: (Project | Task)[];
  public totalPages: number;
  public centralElHeight: string;
  public selectedProjects: Project[] = [];
  public pageInstance: Project | Task;
  public parentPageInstance: Project | Task;
  public grandparentPageInstance: Project;
  public routeType: string;


  @ViewChild('headerElement', {static: false})
  public headerElement: ElementRef;
  @ViewChild('centralElement', {static: false})
  public centralElement: ElementRef;
  @ViewChild('footerElement', {static: false})
  public footerElement: ElementRef;
  @ViewChild('selectionContainer', {static: false})
  public selectionContainer: ElementRef;
  @ViewChild(MatMenuTrigger, {static: false})
  public trigger: MatMenuTrigger;

  constructor(public injector: Injector) {
    this.dataService = injector.get(DataService);
    this.tableService = injector.get(TableService);
    this.activatedRoute = injector.get(ActivatedRoute);
    this.dialog = injector.get(MatDialog);
    this.cdr = injector.get(ChangeDetectorRef);
    this.navigationService = injector.get(NavigationService);
  }

  @HostListener('window:resize')
  public onResize(): void {
    this.resizeComponentsHeight();
  }

  ngOnInit(): void {
    /** Subscribe on showing subProjects */
    this.subscription.add(this.tableService.showNestedItems$.subscribe((value: boolean) => this.showSubProjects = value));

    /** Subscribe on receiving users */
    this.subscription.add(this.dataService.getUsers().subscribe((users: User[]) => this.users = users));
  }

  ngAfterViewChecked(): void {
    /** Set available height for TableComponent */
    this.resizeComponentsHeight();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public setColumns(initialColumns: string[], columns: string[]): void {
    this.columns = columns;
    this.tableDataColumns = initialColumns;
    /** Initial state, value passed to ColumnSelectionComponent */
    this.tableDataColumns.forEach((item: string) => this.columnSelected.select(item));
  }

  public fetchPageProject(): void {
    this.subscription.add(
      this.dataService.getProjects(this.currentPage || INITIAL_PAGE).subscribe((pageProject: PageProject) => {
        this.data = pageProject.content;
        this.totalPages = pageProject.totalPages;
      })
    );
  }

  public fetchProject(): void {
    const {projectId} = this.activatedRoute.snapshot.params;
    this.subscription.add(this.dataService.getProjectById(projectId).subscribe((project: Project) => {
        this.pageInstance = project;
        const {subprojects, id} = project;
        if (subprojects.length) {
          this.data = subprojects;
          this.setColumns(INITIAL_PROJECTS_TABLE_COLUMNS_NAME, PROJECTS_TABLE_COLUMNS_NAME);
          this.routeType = PROJECT_ROUTE;
        } else {
          this.fetchTasks(id);
        }
      })
    );
  }

  public fetchTasks(projectId: number): void {
    this.subscription.add(this.dataService.getTasksByProjectId(projectId, this.currentPage || INITIAL_PAGE)
      .subscribe((pageTask: PageTask) => {
        if (!pageTask.content.length) {
          this.data = [];
          this.setColumns(INITIAL_PROJECTS_TABLE_COLUMNS_NAME, PROJECTS_TABLE_COLUMNS_NAME);
          this.routeType = PROJECT_ROUTE;
          /** Project has tasks */
        } else {
          this.data = pageTask.content;
          this.setColumns(INITIAL_TASKS_TABLE_COLUMNS_NAME, TASKS_TABLE_COLUMNS_NAME);
          this.totalPages = pageTask.totalPages;
          this.routeType = PROJECT_WITH_TASKS_ROUTE;
        }
      })
    );
  }

  public fetchSubProject(): void {
    const {projectId, subprojectId} = this.activatedRoute.snapshot.params;
    const project$ = this.dataService.getProjectById(projectId);
    const subProject$ = this.dataService.getProjectById(subprojectId);
    const tasks$ = this.dataService.getTasksByProjectId(subprojectId, this.currentPage || INITIAL_PAGE);
    this.subscription.add(zip(project$, subProject$, tasks$)
      .pipe(map(([project, subProject, tasks]) => ({project, subProject, tasks})))
      .subscribe(({project, subProject, tasks}) => {
        this.setColumns(INITIAL_TASKS_TABLE_COLUMNS_NAME, TASKS_TABLE_COLUMNS_NAME);
        this.pageInstance = subProject;
        this.parentPageInstance = project;
        this.data = tasks.content;
        this.totalPages = tasks.totalPages;
      })
    );
  }

  public fetchFeature(): void {
    const {projectId, subprojectId, featureId} = this.activatedRoute.snapshot.params;
    const project$ = this.dataService.getProjectById(projectId);
    const subProject$ = subprojectId ? this.dataService.getProjectById(subprojectId) : of(null);
    const feature$ = this.dataService.getTaskById(featureId);
    this.subscription.add(zip(project$, subProject$, feature$)
      .pipe(map(([project, subProject, feature]) => ({project, subProject, feature})))
      .subscribe(({project, subProject, feature}) => {
        this.setColumns(INITIAL_TASKS_TABLE_COLUMNS_NAME, TASKS_TABLE_COLUMNS_NAME);
        this.pageInstance = feature;
        this.parentPageInstance = subProject ? subProject : project;
        this.grandparentPageInstance = subProject ? project : null;
        this.data = feature.subtasks;
      })
    );
  }

  /** Calculate available height for TableComponent */
  public resizeComponentsHeight(): void {
    const clientWidth = document.documentElement.clientWidth;
    const clientHeight = document.documentElement.clientHeight;
    const headerElementHeight = this.headerElement.nativeElement.getBoundingClientRect().height;
    const tableElementHeight = this.centralElement.nativeElement.getBoundingClientRect().height;
    const footerElementHeight = this.footerElement.nativeElement.getBoundingClientRect().height;
    const availableTabletHeight = clientHeight - headerElementHeight - footerElementHeight;
    const availableMobileHeight = clientHeight - headerElementHeight - footerElementHeight - MOBILE_HEADER_HEIGHT;
    const shouldChangeTabletHeight
      = clientWidth < TABLET_MAX_WIDTH
      && clientWidth >= MOBILE_MAX_WIDTH
      && tableElementHeight <= availableTabletHeight;

    if (clientWidth > TABLET_MAX_WIDTH || shouldChangeTabletHeight) {
      this.centralElHeight = availableTabletHeight + 'px';
    } else if (clientWidth < MOBILE_MAX_WIDTH && tableElementHeight <= availableMobileHeight) {
      this.centralElHeight = availableMobileHeight + 'px';
    } else {
      this.centralElHeight = 'auto';
    }

    this.cdr.detectChanges();
  }

  /** Filter table columns by received value from ColumnSelectionComponent */
  public onColumnsChange(selection: SelectionModel<string>): void {
    this.columnSelected = selection;
    const filter = selection.selected;
    this.tableDataColumns = this.columns.filter(item => filter.includes(item));
  }

  /** Calculate new position for container (anchor) of ColumnSelectionComponent */
  public setColumnSelectionPosition(position: DOMRect): void {
    const {top, left} = position;
    const container = this.selectionContainer.nativeElement;
    container.style.top = `${top}px`;
    container.style.left = `${left}px`;
  }

  /** Receive next page from PaginatorComponent */
  public updateCurrentPage(page: number): void {
    if (this.currentPage === page) {
      return;
    }
    this.currentPage = page;
  }

  public navigateToSelectedRow(row): void {
    this.navigationService.navigateToRouteBySelectedRow(row);
  }

  public archiveElements(archiveData: ArchiveData, onArchive: (isArchive: boolean) => void): void {
    const config = {
      width: '480px',
      data: {type: 'archive', selected: archiveData.ids.length}
    };
    this.subscription.add(this.dialog.open(ArchiveJoinComponent, config).afterClosed().subscribe(onArchive.bind(this)));
  }

  public onJoin(event): void {
    console.log(event);
    const projects = []; // receive from server
    const config = {
      width: '480px',
      data: {type: 'joinChecked', projects}
    };
    this.subscription.add(this.dialog.open(ArchiveJoinComponent, config).afterClosed().subscribe(res => console.log(res)));
  }

  public rowSelected(selection): void {
    this.selectedProjects = selection;
  }

  public deleteElement(element, onDelete: () => void): void {
    const {type, id} = element;
    const config = {
      width: '480px',
      data: {type}
    };
    this.subscription.add(this.dialog.open(DeleteComponent, config).afterClosed()
      .subscribe(del => {
        if (del) {
          this.dataService.deleteInstance(id, type).subscribe(onDelete.bind(this));
        }
      })
    );
  }

  public createProject(type: string, callback: (project: Project) => void): void {
    const managers$ = this.dataService.getManagerEmails();
    const groups$ = of([]); // receive from server
    this.subscription.add(zip(managers$, groups$)
      .pipe(map(([managers, groups]) => ({managers, groups})))
      .subscribe(({managers, groups}) => {
        const config = {
          width: '480px',
          data: {type, groups, managers, users: this.users}
        };
        this.dialog.open(CreateEditComponent, config).afterClosed()
          .subscribe((project: Project) => {
            if (!project) {
              return;
            }
            if (type === CREATE_SUBPROJECT) {
              project.parentId = this.pageInstance.id;
            }
            this.dataService.createInstance(project, 'PROJECT').subscribe(callback.bind(this));
          });
      })
    );
  }

  public createTask(type: string, callback: (task: Task) => void): void {
    this.subscription.add(this.dataService.getAllTasks()
      .subscribe((tasks: Task[]) => {
        const config = {
          width: '480px',
          data: {type, users: this.users, tasks}
        };
        this.dialog.open(CreateEditComponent, config).afterClosed()
          .subscribe((task: Task) => {
            if (!task) {
              return;
            }
            switch (type) {
              case CREATE_TASK:
                task.projectId = this.pageInstance.id;
                break;
              case CREATE_SUBTASK:
                task.projectId = this.parentPageInstance.id;
                task.parentId = this.pageInstance.id;
            }
            this.dataService.createTask(task).subscribe(callback.bind(this));
          });
      })
    );
  }

  public onJoinInFeature(ids: number[]): void {
    const config = {
      width: '480px',
      data: {type: CREATE_FEATURE, users: this.users}
    };
    this.subscription.add(this.dialog.open(CreateFeatureComponent, config).afterClosed()
      .subscribe((feature: Task) => {
        if (!feature) {
          return;
        }
        const {projectId, subprojectId} = this.activatedRoute.snapshot.params;
        feature.projectId = subprojectId ? subprojectId : projectId;
        this.dataService.createTask(feature).subscribe((task: Task) => {
          this.dataService.moveTasks(ids, task.projectId, task.id).subscribe(() => this.fetchTasks(task.projectId));
        });
      })
    );
  }

  public onChangesLog(projectId: number): void {
    this.subscription.add(this.dataService.getChangesLogById(projectId, INITIAL_PAGE)
      .subscribe((pageChangesLog: PageChangeLog) => {
        const config = {
          width: '780px',
          data: {pageChangesLog, id: projectId, route: this.routeType}
        };
        this.dialog.open(ChangesLogComponent, config);
      })
    );
  }

  public onMove(event): void {
    console.log(event);
  }

  public editProject(instance: Project, type: string, callback: (project: Project) => void, onArchive: (isArchive: boolean) => void): void {
    const managers$ = this.dataService.getManagerEmails();
    const groups$ = of([]); // receive from server
    this.subscription.add(zip(managers$, groups$)
      .pipe(map(([managers, groups]) => ({managers, groups})))
      .subscribe(({managers, groups}) => {
        const config = {
          width: '480px',
          data: {type, groups, managers, users: this.users, instance}
        };
        this.dialog.open(CreateEditComponent, config).afterClosed()
          .subscribe((result) => {
            if (!result) {
              return;
            } else if (typeof result === 'number') {
              const archiveData: ArchiveData = {
                ids: [this.pageInstance.id],
                type: ROOT_ELEMENT,
                instance: this.pageInstance.type
              };
              this.archiveElements(archiveData, onArchive);
            } else {
              if (type === EDIT_SUBPROJECT) {
                result.parentId = this.parentPageInstance.id;
              }
              this.dataService.editProject(result).subscribe(callback.bind(this));
            }
          });
      })
    );
  }

  public editTask(instance: Task, type: string, callback: (task: Task) => void, onArchive: (isArchive: boolean) => void): void {
    this.subscription.add(this.dataService.getAllTasks()
      .subscribe((tasks: Task[]) => {
        const config = {
          width: '480px',
          data: {type, users: this.users, tasks, instance}
        };
        this.dialog.open(CreateEditComponent, config).afterClosed()
          .subscribe((result) => {
            if (!result) {
              return;
            } else if (typeof result === 'number') {
              const archiveData: ArchiveData = {ids: [instance.id], type: ROOT_ELEMENT, instance: instance.type};
              this.archiveElements(archiveData, onArchive);
            } else {
              this.dataService.editTask(result, instance.id).subscribe(callback.bind(this));
            }
          });
      })
    );
  }
}
