import { Directive, Input, OnInit, OnDestroy, ElementRef, Renderer2 } from '@angular/core';
import { TableService } from '../services/table.service';
import Project from '../models/Project.model';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import Task from '../models/Task.model';

@Directive({
  selector: '[ptsRowHeight]'
})
export class RowHeightDirective implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  @Input() private element: any;
  @Input() private column: string;

  constructor(
    private tableService: TableService,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private activatedRoute: ActivatedRoute,
    ) { }

  ngOnInit(): void {
    const { projectId, subprojectId, featureId } = this.activatedRoute.snapshot.params;
    const isProjectRoute = projectId && !subprojectId;
    const rowElement = this.elementRef.nativeElement;
    const { parentId, subtasks, type } = this.element;

    /** Subscribe on changing nested elements visibility, receive data */
    this.subscription.add(this.tableService.showNestedItems$.subscribe((value: boolean) => this.toggleClass(value)));

    /** Toggle CSS classes for table row (height) */
    if (isProjectRoute || !parentId) {
      this.renderer.addClass(rowElement, 'table-general-row');
    }

    /** Toggle CSS classes for table row (padding-left for name column) */
    if (this.column !== 'name' || (this.column === 'name' && !parentId) || featureId) {
      this.renderer.addClass(rowElement, 'table-subproject-row');
    }

    if ((type === 'TASK' || type === 'SUBTASK') && (parentId || subtasks.length)) {
      this.renderer.removeClass(rowElement, 'table-general-row');
    }

    if (type === 'SUBTASK' && featureId) {
      this.renderer.addClass(rowElement, 'table-general-row');
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  /** Toggle CSS classes for table row (height of row) */
  private toggleClass(value: boolean): void {
    const rowElement = this.elementRef.nativeElement;
    if (value && this.element.subprojects.length) {
      this.renderer.removeClass(rowElement, 'table-general-row');
    } else {
      this.renderer.addClass(rowElement, 'table-general-row');
    }
  }
}
