import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { BaseListComponent } from '../base-list.component';
import CreateEditData from '../../../../models/CreateEditData.model';
import ArchiveData from '../../../../models/ArchiveData.model';
import { CHILD_ELEMENT, EDIT_TASK, ROOT_ELEMENT } from '../../../../constants';
import Task from '../../../../models/Task.model';

@Component({
  selector: 'pts-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['../base-list.component.scss']
})
export class FeatureListComponent extends BaseListComponent implements OnInit, OnDestroy {
  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.fetchFeature();
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
  }

  public onCreate(createData: CreateEditData): void {
    this.createTask(createData.type, () => this.fetchFeature());
  }

  public onArchive(archiveData: ArchiveData): void {
    this.archiveElements(archiveData, (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      const {ids, type} = archiveData;
      this.subscription.add(this.dataService.archiveTasks(ids, this.parentPageInstance.id)
        .subscribe(() => {
          switch (type) {
            case ROOT_ELEMENT:
              this.navigationService.navigateStepBack();
              break;
            case CHILD_ELEMENT:
              this.fetchFeature();
          }
        })
      );
    });
  }

  public onEdit(feature: Task): void {
    this.editTask(feature, EDIT_TASK, () => this.fetchFeature(), (isArchive: boolean) => {
      if (!isArchive) {
        return;
      }
      this.subscription.add(this.dataService.archiveTasks([feature.id], this.parentPageInstance.id)
        .subscribe(() => this.navigationService.navigateStepBack())
      );
    });
  }

  public delete(element): void {
    this.deleteElement(element, () => this.fetchFeature());
  }
}
