import { Directive, Input, OnInit, ElementRef, Renderer2 } from '@angular/core';
import Project from '../models/Project.model';

@Directive({
  selector: '[ptsNestedRow]'
})
export class NestedRowDirective implements OnInit {
  @Input() private element: Project;
  @Input() private column: string;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) { }

  ngOnInit(): void {
    /** Display nested elements line */
    const lineElement = this.elementRef.nativeElement;
    const { parentId, lastElement } = this.element;
    if (this.column !== 'name' || !parentId) {
      return;
    }
    const className = lastElement ? 'table-elements-nested-line-last' : 'table-elements-nested-line';
    this.renderer.addClass(lineElement, className);
  }
}
