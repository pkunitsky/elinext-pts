import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { BaseListComponent } from '../base-list.component';
import Project from '../../../../models/Project.model';
import Task from '../../../../models/Task.model';
import PageTimeReport from '../../../../models/PageTimeReport.model';
import { of, zip } from 'rxjs';
import { map } from 'rxjs/operators';
import { ADD_REPORT, EDIT_REPORT, EDIT_TASK, INITIAL_PAGE } from '../../../../constants';
import TimeReport from '../../../../models/TimeReport.model';
import { CreateEditReportComponent } from '../../../../components/modals/create-edit-report/create-edit-report.component';
import { DeleteComponent } from '../../../../components/modals/delete/delete.component';
import ArchiveData from '../../../../models/ArchiveData.model';
import CreateEditData from '../../../../models/CreateEditData.model';

@Component({
  selector: 'pts-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent extends BaseListComponent implements OnInit, OnDestroy {
  public project: Project;
  public subProject: Project;
  public feature: Task;
  public task: Task;
  public pageReport: PageTimeReport;

  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.fetchTask();
    this.fetchPageReport();
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
  }

  public fetchTask(): void {
    const {projectId, subprojectId, featureId, taskId} = this.activatedRoute.snapshot.params;
    const project$ = this.dataService.getProjectById(projectId);
    const subProject$ = subprojectId ? this.dataService.getProjectById(subprojectId) : of(null);
    const feature$ = featureId ? this.dataService.getTaskById(featureId) : of(null);
    const task$ = this.dataService.getTaskById(taskId);
    this.subscription.add(zip(project$, subProject$, feature$, task$)
      .pipe(map(([project, subProject, feature, task]) => ({project, subProject, feature, task})))
      .subscribe(({project, subProject, feature, task}) => {
        this.project = project;
        this.subProject = subProject;
        this.feature = feature;
        this.task = task;
      })
    );
  }

  public fetchPageReport(): void {
    const {taskId} = this.activatedRoute.snapshot.params;
    this.subscription.add(this.dataService.getReportsByTaskId(taskId, this.currentPage || INITIAL_PAGE)
      .subscribe((pageReport: PageTimeReport) => {
        this.totalPages = pageReport.totalPages;
        this.pageReport = pageReport;
      })
    );
  }

  public getUserNameByEmail(data: string): string {
    if (this.users.length) {
      const user = this.users.find(({email}) => email === data);
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

  public getFormatDate(date: string): string {
    if (!date) {
      return '';
    }
    const dateArray = date.split(' ');
    return dateArray[0].split('-').reverse().join('.') + ' at ' + dateArray[1];
  }

  public editReport(report: TimeReport): void {
    const config = {
      width: '480px',
      data: {type: EDIT_REPORT, report}
    };
    this.subscription.add(this.dialog.open(CreateEditReportComponent, config).afterClosed()
      .subscribe((timeReport: TimeReport) => {
        if (!timeReport) {
          return;
        }
        timeReport.taskId = report.taskId;
        this.dataService.editReport(timeReport, report.id).subscribe(() => this.fetchPageReport());
      })
    );
  }

  public deleteReport(report): void {
    const config = {
      width: '480px',
      data: {type: 'TIME_REPORT'}
    };
    this.subscription.add(this.dialog.open(DeleteComponent, config).afterClosed()
      .subscribe(del => {
        if (!del) {
          return;
        }
        this.dataService.deleteReport(report.id).subscribe(() => this.fetchPageReport());
      })
    );
  }

  public onEdit(instance: Task): void {
    this.editTask(instance, EDIT_TASK, () => this.fetchTask(), this.onArchivePermission.bind(this));
  }

  public onArchive(archiveData: ArchiveData): void {
    this.archiveElements(archiveData, this.onArchivePermission.bind(this));
  }

  private onArchivePermission(isArchive: boolean): void {
    if (!isArchive) {
      return;
    }
    const projectId = this.subProject ? this.subProject.id : this.project.id;
    this.subscription.add(this.dataService.archiveTasks([this.task.id], projectId)
      .subscribe(() => this.navigationService.navigateStepBack())
    );
  }

  public onAdd(data: CreateEditData): void {
    const config = {
      width: '480px',
      data: {type: ADD_REPORT}
    };
    this.subscription.add(this.dialog.open(CreateEditReportComponent, config).afterClosed()
      .subscribe((report: TimeReport) => {
        if (!report) {
          return;
        }
        report.taskId = data.id;
        this.dataService.createReport(report).subscribe(() => this.fetchPageReport());
      })
    );
  }
}
