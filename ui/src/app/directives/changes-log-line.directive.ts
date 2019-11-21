import { AfterViewInit, Directive, ElementRef, Input, Renderer2 } from '@angular/core';

@Directive({
  selector: '[ptsChangesLogLine]'
})
export class ChangesLogLineDirective implements AfterViewInit {
  @Input() public changesLogLength: number;
  @Input() public index: number;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) { }

  ngAfterViewInit(): void {
    if (this.index === this.changesLogLength - 1) {
      return;
    }
    const element = this.elementRef.nativeElement;
    const parentElementHeight = this.renderer.parentNode(element).offsetHeight + 'px';
    this.renderer.setStyle(element, 'height', parentElementHeight);
  }
}
