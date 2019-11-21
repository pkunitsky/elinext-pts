import {
  AfterViewChecked,
  Component,
  Injector,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { BaseListComponent } from '../base-list.component';
import {
  CREATE_PROJECT,
  INITIAL_PROJECTS_TABLE_COLUMNS_NAME,
  PROJECTS_TABLE_COLUMNS_NAME
} from '../../../../constants';
import ArchiveData from '../../../../models/ArchiveData.model';
import Project from '../../../../models/Project.model';

@Component({
  selector: 'pts-projects-list',
  templateUrl: './root-list.component.html',
  styleUrls: ['../base-list.component.scss']
})
export class RootListComponent extends BaseListComponent implements OnInit, OnDestroy, AfterViewChecked {

  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.setColumns(INITIAL_PROJECTS_TABLE_COLUMNS_NAME, PROJECTS_TABLE_COLUMNS_NAME);
    this.fetchPageProject();
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
    this.subscription.unsubscribe();
  }

  public updatePage(page: number): void {
    this.updateCurrentPage(page);
    this.fetchPageProject();
  }

  public delete(element): void {
    this.deleteElement(element, () => this.fetchPageProject());
  }

  public onArchive(archiveData: ArchiveData): void {
    this.archiveElements(archiveData, (isArchive: boolean) => {
      if (isArchive) {
        this.dataService.archiveProjects(archiveData.ids).subscribe(res => this.fetchPageProject());
      }
    });
  }

  public onCreate(createData): void {
    this.createProject(CREATE_PROJECT, (project: Project) => this.fetchPageProject());
  }
}
