import { Directive, ElementRef, Input, HostListener } from '@angular/core';

@Directive({
  selector: '[ptsTooltip]'
})
export class TooltipDirective {
  @Input() private tooltip;

  constructor(private elementRef: ElementRef) { }

  @HostListener('mouseenter')
  public onMouseEnter(): void {
    const element = this.elementRef.nativeElement;
    /** Check whether text element width more then its container width, show tooltip if it true */
    if (element.scrollWidth > element.offsetWidth) {
      this.tooltip.show();
    }
  }

  @HostListener('mouseleave')
  public onMouseLeave(): void {
    this.tooltip.hide();
  }
}
