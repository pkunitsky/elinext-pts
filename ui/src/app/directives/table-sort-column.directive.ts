import { Directive, Input, OnInit, OnDestroy, ElementRef, Renderer2 } from '@angular/core';
import SortData from '../models/SortData.model';
import { TableService } from '../services/table.service';
import { Subscription } from 'rxjs';

@Directive({
  selector: '[ptsTableSortColumn]'
})
export class TableSortColumnDirective implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  @Input() column: string;

  constructor(
    private tableService: TableService,
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) {
  }

  ngOnInit(): void {
    /** Subscribe on table sorting event, receive sort data */
    this.subscription.add(this.tableService.sortTable$.subscribe((data: SortData) => this.setClass(data)));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  /** Toggle CSS classes for table list-header titles */
  private setClass(data: SortData): void {
    const {sortColumn, sortOrder} = data;
    const element = this.elementRef.nativeElement;
    const ascendElement = element.lastChild.firstChild;
    const descendElement = element.lastChild.lastChild;

    if (this.column === sortColumn) {
      const ascendClass = sortOrder ? 'up-arrow-active' : 'arrow-hidden';
      const descendClass = sortOrder ? 'arrow-hidden' : 'down-arrow-active';
      this.renderer.setAttribute(ascendElement, 'class', ascendClass);
      this.renderer.setAttribute(descendElement, 'class', descendClass);
      this.renderer.addClass(element, 'sorting-column');
    } else {
      this.renderer.removeClass(element, 'sorting-column');
      this.renderer.setAttribute(ascendElement, 'class', 'up-arrow');
      this.renderer.setAttribute(descendElement, 'class', 'down-arrow');
    }
  }
}
