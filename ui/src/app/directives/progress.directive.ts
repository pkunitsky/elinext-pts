import { Directive, ElementRef, HostListener, Input, OnInit, Renderer2 } from '@angular/core';

@Directive({
  selector: '[ptsProgress]'
})
export class ProgressDirective implements OnInit {
  @Input() percentage: number;

  @HostListener('window:resize')
  public onResize(): void {
    this.setWidth();
  }

  constructor(
    private renderer: Renderer2,
    private elementRef: ElementRef
    ) { }

    ngOnInit(): void {
      this.setWidth();
    }

    private setWidth(): void {
      const parentWidth = this.renderer.parentNode(this.elementRef.nativeElement).offsetWidth;
      const svgElement = this.elementRef.nativeElement.firstElementChild;
      const progressBar = svgElement.firstElementChild;
      const progressValue = svgElement.lastElementChild;
      const value = parentWidth * this.percentage / 100;
      this.renderer.setAttribute(svgElement, 'width', parentWidth);
      this.renderer.setAttribute(progressBar, 'width', parentWidth);
      this.renderer.setAttribute(progressValue, 'width', value.toString());
    }
}
