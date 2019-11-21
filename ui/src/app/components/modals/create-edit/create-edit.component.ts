import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import DialogData from '../../../models/DialogData.model';
import Project from '../../../models/Project.model';
import {
  EDIT_PROJECT,
  CREATE_SUBPROJECT,
  CREATE_PROJECT,
  EDIT_SUBPROJECT,
  CREATE_TASK,
  PRIORITY_TYPES,
  EDIT_TASK,
  STATUS, CREATE_SUBTASK
} from '../../../constants';
import Task from '../../../models/Task.model';
import User from '../../../models/User.model';

@Component({
  selector: 'pts-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  public form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    projectLeadEmail: [''],
    startDate: [''],
    deadline: [''],
    status: [''],
    description: [''],
    customer: [''],
    groupName: [''],
    managerEmail: [''],
    assigneeEmail: [''],
    estimatedTime: [''],
    percentage: [''],
    priority: [''],
    parentId: [''],
  }, {});

  public isAdvancedSettingsShown = false;
  private newProject: Project = {name: ''};
  public priorityTypes = PRIORITY_TYPES;
  public statusTypes = STATUS;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<CreateEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      if (this.isEditModal() || this.isTaskEdit()) {
        this.form.patchValue(this.parseProject(this.data.instance));
      }
    });
  }

  /** Toggle advanced setting block */
  public toggleAdvancedSettings(): void {
    this.isAdvancedSettingsShown = !this.isAdvancedSettingsShown;
  }

  /** Fire "focus" and "blur" event when "dateChange" was emitted, to check input value */
  public checkValue(event): void {
    event.targetElement.focus();
    event.targetElement.blur();
  }

  /** Get "name" field of form */
  public get name(): FormControl {
    return this.form.get('name') as FormControl;
  }

  /** Get "lead" field of form */
  public get lead(): FormControl {
    return this.form.get('projectLeadEmail') as FormControl;
  }

  /** Get "startDate" field of form */
  public get startDate(): FormControl {
    return this.form.get('startDate') as FormControl;
  }

  /** Get "deadline" field of form */
  public get deadline(): FormControl {
    return this.form.get('deadline') as FormControl;
  }

  /** Get "status" field of form */
  public get status(): FormControl {
    return this.form.get('status') as FormControl;
  }

  /** Get "description" field of form */
  public get description(): FormControl {
    return this.form.get('description') as FormControl;
  }

  /** Get "customer" field of form */
  public get customer(): FormControl {
    return this.form.get('customer') as FormControl;
  }

  /** Get "groupName" field of form */
  public get groupName(): FormControl {
    return this.form.get('groupName') as FormControl;
  }

  /** Get "managerEmail" field of form */
  public get managerEmail(): FormControl {
    return this.form.get('managerEmail') as FormControl;
  }

  /** Get "assigneeEmail" field of form */
  public get assigneeEmail(): FormControl {
    return this.form.get('assigneeEmail') as FormControl;
  }

  /** Get "estimatedTime" field of form */
  public get estimatedTime(): FormControl {
    return this.form.get('estimatedTime') as FormControl;
  }

  /** Get "percentage" field of form */
  public get percentage(): FormControl {
    return this.form.get('percentage') as FormControl;
  }

  /** Get "priority" field of form */
  public get priority(): FormControl {
    return this.form.get('priority') as FormControl;
  }

  /** Get "parentId" field of form */
  public get parentId(): FormControl {
    return this.form.get('parentId') as FormControl;
  }

  public getProject(): object {
    const formData = this.form.value;
    Object.keys(formData).forEach(key => {
      if (!formData[key]) {
        return;
      }
      if (key === 'startDate' || key === 'deadline') {
        this.newProject[key] = this.formatDate(formData[key]);
      } else {
        this.newProject[key] = formData[key];
      }
    });
    if (this.isEditModal()) {
      this.newProject.id = this.data.instance.id;
    }
    return this.newProject;
  }

  private formatDate(date: Date): string {
    const yearMonthDay = new Intl.DateTimeFormat('en-GB', {year: 'numeric', month: '2-digit', day: '2-digit'})
      .format(date).split('/').reverse().join('-');
    const hoursMinutes = new Intl.DateTimeFormat('en-GB', {hour: '2-digit', minute: '2-digit'}).format(date);
    return `${yearMonthDay} ${hoursMinutes}`;
  }

  private parseProject(project: (Project | Task)): (Project | Task) {
    const data = {...project};
    const { startDate, deadline } = data;
    if (startDate) {
      data.startDate = this.parseDate(startDate);
    }
    if (deadline) {
      data.deadline = this.parseDate(deadline);
    }
    return data;
  }

  private parseDate(date: string): any {
    return new Date(date.split(' ')[0] + 'T00:00:00');
  }

  public getButtonText(): string {
    switch (this.data.type) {
      case EDIT_PROJECT:
      case EDIT_SUBPROJECT:
      case EDIT_TASK:
        return 'SAVE CHANGES';
      case CREATE_PROJECT:
        return 'CREATE PROJECT';
      case CREATE_SUBPROJECT:
        return 'CREATE SUBPROJECT';
      case CREATE_TASK:
        return 'CREATE TASK';
      case CREATE_SUBTASK:
        return 'CREATE SUBTASK';
    }
  }

  public getTitleText(): string {
    switch (this.data.type) {
      case EDIT_PROJECT:
        return 'Edit project';
      case CREATE_PROJECT:
        return 'Create project';
      case CREATE_SUBPROJECT:
        return 'Create subproject';
      case EDIT_SUBPROJECT:
        return 'Edit subproject';
      case CREATE_TASK:
        return 'Create task';
      case EDIT_TASK:
        return 'Edit task';
      case CREATE_SUBTASK:
        return 'Create subtask';
    }
  }

  public getTypeText(): string {
    if (this.isProjectModal()) {
      return 'Project';
    } else if (this.isTaskModal()) {
      return 'Task';
    } else {
      return 'SubProject';
    }
  }

  public isEditModal(): boolean {
    const { type } = this.data;
    return type === EDIT_PROJECT || type === EDIT_SUBPROJECT;
  }

  public isTaskModal(): boolean {
    const { type } = this.data;
    return type === CREATE_TASK || type === EDIT_TASK || type === CREATE_SUBTASK;
  }

  public isProjectModal(): boolean {
    const { type } = this.data;
    return type === EDIT_PROJECT || type === CREATE_PROJECT;
  }

  public getUserOption(userEmail: string): string {
    const user = this.data.users.find((item: User) => item.email === userEmail);
    const {email, firstName, lastName} = user;
    return `${firstName} ${lastName} (${email})`;
  }

  public getAdvancedSettingsCssClass(): string {
    const heightClass = this.isTaskModal() && !this.isSubTask() ? 'advanced-visible-task' : 'advanced-visible';
    return this.isAdvancedSettingsShown ? heightClass : 'advanced-hidden';
  }

  public getDisabledStatus(): boolean {
    return this.isTaskModal() ? this.name.invalid || this.description.invalid : this.name.invalid;
  }

  public isTaskEdit(): boolean {
    return this.data.type === EDIT_TASK;
  }

  public isSubTask(): boolean {
    return this.data.type === CREATE_SUBTASK;
  }
}
