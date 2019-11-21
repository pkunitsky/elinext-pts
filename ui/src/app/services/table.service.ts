import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import SortData from '../models/SortData.model';

@Injectable({
  providedIn: 'root'
})
export class TableService {
  private sortTable = new Subject<SortData>();
  public sortTable$ = this.sortTable.asObservable();

  private showNestedItems = new Subject<boolean>();
  public showNestedItems$ = this.showNestedItems.asObservable();

  constructor() { }

  public tableSorted(sortData: SortData): void {
    this.sortTable.next(sortData);
  }

  public toggleNestedItems(value: boolean): void {
    this.showNestedItems.next(value);
  }
}
