import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import DialogData from '../../../models/DialogData.model';
import { PRIORITY_TYPES } from '../../../constants';
import Task from '../../../models/Task.model';

@Component({
  selector: 'pts-create-feature',
  templateUrl: './create-feature.component.html',
  styleUrls: ['./create-feature.component.scss']
})
export class CreateFeatureComponent implements OnInit {
  public form: FormGroup = this.formBuilder.group({
    description: ['', [Validators.required]],
    name: ['', [Validators.required]],
    priority: [''],
    startDate: [''],
    deadline: ['']
  }, {});

  public isAdvancedSettingsShown = false;
  public priorityTypes: string[] = PRIORITY_TYPES;
  private newFeature: Task = {name: '', description: ''};

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<CreateFeatureComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) { }

  ngOnInit() {
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

  /** Get "description" field of form */
  public get description(): FormControl {
    return this.form.get('description') as FormControl;
  }

  /** Get "name" field of form */
  public get name(): FormControl {
    return this.form.get('name') as FormControl;
  }

  /** Get "priority" field of form */
  public get priority(): FormControl {
    return this.form.get('priority') as FormControl;
  }

  /** Get "startDate" field of form */
  public get startDate(): FormControl {
    return this.form.get('startDate') as FormControl;
  }

  /** Get "deadline" field of form */
  public get deadline(): FormControl {
    return this.form.get('deadline') as FormControl;
  }

  public getFeature(): Task {
    const formData = this.form.value;
    Object.keys(formData).forEach(key => {
      if (!formData[key]) {
        return;
      }
      if (key === 'startDate' || key === 'deadline') {
        this.newFeature[key] = this.formatDate(formData[key]);
      } else {
        this.newFeature[key] = formData[key];
      }
    });
    return this.newFeature;
  }

  private formatDate(date: Date): string {
    const yearMonthDay = new Intl.DateTimeFormat('en-GB', {year: 'numeric', month: '2-digit', day: '2-digit'})
      .format(date).split('/').reverse().join('-');
    const hoursMinutes = new Intl.DateTimeFormat('en-GB', {hour: '2-digit', minute: '2-digit'}).format(date);
    return `${yearMonthDay} ${hoursMinutes}`;
  }

  public getDisabledStatus(): boolean {
    return this.name.invalid || this.description.invalid;
  }
}
