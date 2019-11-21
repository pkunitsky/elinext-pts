import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import DialogData from '../../../models/DialogData.model';
import { DataService } from '../../../services/data.service';
import { Subscription } from 'rxjs';
import PageChangeLog from '../../../models/PageChangeLog.model';
import ChangeLog from '../../../models/ChangeLog.model';
import { PROJECT_ROUTE, SUBPROJECT_ROUTE } from '../../../constants';

@Component({
  selector: 'pts-changes-log',
  templateUrl: './changes-log.component.html',
  styleUrls: ['./changes-log.component.scss']
})
export class ChangesLogComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  public changesLog: ChangeLog[] = [];

  constructor(
    public dialogRef: MatDialogRef<ChangesLogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private dataService: DataService
  ) {}

  ngOnInit() {
    this.changesLog = this.parseChangesLog(this.data.pageChangesLog.content);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public updateCurrentPage(page: number): void {
    this.subscription.add(this.dataService.getChangesLogById(this.data.id, page)
      .subscribe((pageChangesLog: PageChangeLog) => this.changesLog = this.parseChangesLog(pageChangesLog.content))
    );
  }

  private parseChangesLog(data: ChangeLog[]): ChangeLog[] {
    return data.map(log => {
      const changes = [];
      for (const key in log.values) {
        if (key !== 'changedDate' && key !== 'code') {
          changes.push({[key]: log.values[key]});
        }
      }
      return {...log, values: changes};
    });
  }

  public getLogType(): string {
    if (this.data.route === PROJECT_ROUTE) {
      return 'project';
    } else if (this.data.route === SUBPROJECT_ROUTE) {
      return 'subproject';
    }
  }
  public getChangeFieldName(event: object): string {
    return Object.keys(event)[0];
  }

  public getChangeFieldValue(event: object): string {
    const eventKey = Object.keys(event)[0];
    const lastValue = event[eventKey][0];
    const nextValue = event[eventKey][1];
    return lastValue ? `${lastValue} > ${nextValue}` : nextValue;
  }

  public getDate(date: string): string {
    const dateArray = date.split(' ');
    return dateArray[0].split('-').reverse().join('.') + ' at ' + dateArray[1];
  }
}
