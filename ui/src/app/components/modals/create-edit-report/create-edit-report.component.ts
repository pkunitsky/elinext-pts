import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import DialogData from '../../../models/DialogData.model';
import { EDIT_REPORT, STATUS } from '../../../constants';
import TimeReport from '../../../models/TimeReport.model';

@Component({
  selector: 'pts-create-edit-report',
  templateUrl: './create-edit-report.component.html',
  styleUrls: ['./create-edit-report.component.scss']
})
export class CreateEditReportComponent implements OnInit {
  public form: FormGroup = this.formBuilder.group({
    reportedDate: ['', [Validators.required]],
    comment: ['', [Validators.required]],
    hours: ['', [Validators.required]],
    percentage: [''],
    taskStatus: ['', [Validators.required]]
  }, {});

  public statusTypes = STATUS;
  public isAdvancedSettingsShown = false;
  private newReport: TimeReport = {comment: '', hours: null, reportedDate: '', taskId: null, taskStatus: 'SUBMITTED'};

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<CreateEditReportComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) { }

  ngOnInit(): void {
    setTimeout(() => {
      if (this.isEditType()) {
        this.form.patchValue(this.parseReport(this.data.report));
      }
    });
  }

  private parseReport(report: TimeReport): TimeReport {
    const data = {...report};
    const { reportedDate } = data;
    if (reportedDate) {
      data.reportedDate = this.parseDate(reportedDate);
    }
    return data;
  }

  private parseDate(date: string): any {
    return new Date(date.split(' ')[0] + 'T00:00:00');
  }

  public isEditType(): boolean {
    return this.data.type === EDIT_REPORT;
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

  /** Get "reportedDate" field of form */
  public get reportedDate(): FormControl {
    return this.form.get('reportedDate') as FormControl;
  }

  /** Get "comment" field of form */
  public get comment(): FormControl {
    return this.form.get('comment') as FormControl;
  }

  /** Get "hours" field of form */
  public get hours(): FormControl {
    return this.form.get('hours') as FormControl;
  }

  /** Get "percentage" field of form */
  public get percentage(): FormControl {
    return this.form.get('percentage') as FormControl;
  }

  /** Get "taskStatus" field of form */
  public get taskStatus(): FormControl {
    return this.form.get('taskStatus') as FormControl;
  }

  public getReport(): TimeReport {
    const formData = this.form.value;
    Object.keys(formData).forEach(key => {
      if (!formData[key]) {
        return;
      }
      if (key === 'reportedDate') {
        this.newReport[key] = this.formatDate(formData[key]);
      } else {
        this.newReport[key] = formData[key];
      }
    });
    return this.newReport;
  }

  private formatDate(date: Date): string {
    const yearMonthDay = new Intl.DateTimeFormat('en-GB', {year: 'numeric', month: '2-digit', day: '2-digit'})
      .format(date).split('/').reverse().join('-');
    const hoursMinutes = new Intl.DateTimeFormat('en-GB', {hour: '2-digit', minute: '2-digit'}).format(date);
    return `${yearMonthDay} ${hoursMinutes}`;
  }

  public getDisabledStatus(): boolean {
    return this.reportedDate.invalid || this.comment.invalid || this.hours.invalid || this.taskStatus.invalid;
  }
}
