import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { BaseListComponent } from '../base-list.component';
import ArchiveData from '../../../../models/ArchiveData.model';
import { CHILD_ELEMENT, EDIT_SUBPROJECT, ROOT_ELEMENT } from '../../../../constants';
import Project from '../../../../models/Project.model';
import CreateEditData from '../../../../models/CreateEditData.model';

@Component({
  selector: 'pts-subproject-list',
  templateUrl: './subproject-list.component.html',
  styleUrls: ['../base-list.component.scss']
})
export class SubprojectListComponent extends BaseListComponent implements OnInit, OnDestroy {

  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit() {
    super.ngOnInit();
    this.fetchSubProject();
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
  }

  public onArchive(archiveData: ArchiveData): void {
    this.archiveElements(archiveData, (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      const {ids, type} = archiveData;
      switch (type) {
        case ROOT_ELEMENT:
          this.subscription.add(this.dataService.archiveProjects(ids)
            .subscribe(() => this.navigationService.navigateStepBack())
          );
          break;
        case CHILD_ELEMENT:
          this.subscription.add(this.dataService.archiveTasks(ids, this.pageInstance.id)
            .subscribe(() => this.fetchTasks(this.pageInstance.id))
          );
      }
    });
  }

  public delete(element): void {
    this.deleteElement(element, () => this.fetchTasks(this.pageInstance.id));
  }

  public onEdit(subProject: Project): void {
    this.editProject(subProject, EDIT_SUBPROJECT, () => this.fetchProject(), (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      this.subscription.add(this.dataService.archiveProjects([subProject.id])
        .subscribe(() => this.navigationService.navigateStepBack())
      );
    });
  }

  public updatePage(page: number): void {
    this.updateCurrentPage(page);
    this.fetchTasks(this.pageInstance.id);
  }

  public onCreate(createData: CreateEditData): void {
    const {type, id} = createData;
    this.createTask(type, () => this.fetchTasks(id));
  }
}
