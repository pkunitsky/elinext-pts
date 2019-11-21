import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { BaseListComponent } from '../base-list.component';
import { CHILD_ELEMENT, CREATE_SUBPROJECT, CREATE_TASK, EDIT_PROJECT, ROOT_ELEMENT} from '../../../../constants';
import ArchiveData from '../../../../models/ArchiveData.model';
import CreateEditData from '../../../../models/CreateEditData.model';
import Project from '../../../../models/Project.model';

@Component({
  selector: 'pts-project-with-subprojects-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['../base-list.component.scss']
})
export class ProjectListComponent extends BaseListComponent implements OnInit, OnDestroy {

  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.fetchProject();
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
  }

  public onArchive(archiveData: ArchiveData): void {
    this.archiveElements(archiveData, (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      const {ids, type, instance} = archiveData;
      switch (type) {
        case ROOT_ELEMENT:
          this.subscription.add(this.dataService.archiveProjects(ids)
            .subscribe(() => this.navigationService.navigateStepBack())
          );
          break;
        case CHILD_ELEMENT:
          this.subscription.add(this.dataService.archiveInstance(ids, instance, this.pageInstance.id)
            .subscribe(() => this.fetchProject())
          );
      }
    });
  }

  public onCreate(createData: CreateEditData): void {
    const {type, id} = createData;
    switch (type) {
      case CREATE_SUBPROJECT:
        this.createProject(CREATE_SUBPROJECT, () => this.fetchProject());
        break;
      case CREATE_TASK:
        this.createTask(CREATE_TASK, () => this.fetchTasks(id));
    }
  }

  public delete(element): void {
    this.deleteElement(element, () => this.fetchProject());
  }

  public updatePage(page: number): void {
    this.updateCurrentPage(page);
    this.fetchTasks(this.pageInstance.id);
  }

  public onEdit(project: Project): void {
    this.editProject(project, EDIT_PROJECT, () => this.fetchProject(), (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      this.subscription.add(this.dataService.archiveProjects([project.id])
        .subscribe(() => this.navigationService.navigateStepBack())
      );
    });
  }
}
