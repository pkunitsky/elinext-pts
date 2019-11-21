import { Component, Input } from '@angular/core';
import { SELECTION_OPTIONS } from '../../constants';
import User from '../../models/User.model';
import { formatDate } from '@angular/common';

@Component({
  selector: 'pts-additional-info',
  templateUrl: './additional-info.component.html',
  styleUrls: ['./additional-info.component.scss']
})
export class AdditionalInfoComponent {
  @Input() public field: string;
  @Input() public value: string;
  @Input() public lastItem: boolean;
  @Input() public users: User[] = [];

  public getFieldTitle(): string {
    return SELECTION_OPTIONS[this.field];
  }

  public isLeadField(): boolean {
    return this.field === 'projectLeadEmail';
  }

  public getFieldText(): any {
    switch (this.field) {
      case 'projectLeadEmail':
      case 'managerEmail':
      case 'assigneeEmail':
        return this.getEmailTextNode(this.value);
      case 'changedDate':
      case 'deadline':
      case 'startDate':
        return this.getFormatDate(this.value);
      case 'estimatedTime':
        return this.value + ' hours';
      case 'percentage':
        return this.value + ' %';
      default:
        return this.value;
    }
  }

  private getEmailTextNode(data: string): string {
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
    const parseDate = new Date(dateArray[0] + 'T' + dateArray[1] + ':00');
    return formatDate(parseDate, 'd MMMM y, hh:mm', 'en-US').split(',').join(' at');
  }
}
