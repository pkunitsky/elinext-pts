import { Component, EventEmitter, Injector, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import Project from '../../../../../models/Project.model';
import { CHILD_ELEMENT, CREATE_PROJECT } from '../../../../../constants';
import ArchiveData from '../../../../../models/ArchiveData.model';
import { BaseHeaderComponent } from '../base-header.component';
import CreateEditData from '../../../../../models/CreateEditData.model';

@Component({
  selector: 'pts-projects-list-header',
  templateUrl: './root-header.component.html',
  styleUrls: ['./root-header.component.scss']
})
export class RootHeaderComponent extends BaseHeaderComponent implements OnInit, OnChanges {
  public archiveData: ArchiveData;
  public createProjectData: CreateEditData;

  @Input() public selected: Project [] = [];

  @Output() createProject = new EventEmitter();
  @Output() archive = new EventEmitter();
  @Output() joinProject = new EventEmitter();

  constructor(public injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    this.createProjectData = {type: CREATE_PROJECT};
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.selected.currentValue.length) {
      this.archiveData = {
        ids: this.selected.map(project => project.id), type: CHILD_ELEMENT, instance: 'PROJECT'
      };
    }
  }
}
