import { Directive, ElementRef, OnDestroy, OnInit, Renderer2 } from '@angular/core';

@Directive({
  selector: '[ptsFloatLabel]'
})
export class FloatLabelDirective implements OnInit, OnDestroy {
  private onFocusListener: () => void;
  private onBlurListener: () => void;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) {
  }

  ngOnInit(): void {
    const inputElement = this.elementRef.nativeElement.lastChild;
    /** Subscribe to "focus" and "blur" events */
    this.onFocusListener = this.renderer.listen(inputElement, 'focus', this.addActiveClass);
    this.onBlurListener = this.renderer.listen(inputElement, 'blur', this.removeActiveClass);
  }

  ngOnDestroy(): void {
    /** Unsubscribe from "focus" and "blur" events */
    this.onBlurListener();
    this.onBlurListener();
  }

  /** "focus" event callback */
  private addActiveClass = () => {
    this.renderer.addClass(this.elementRef.nativeElement, 'active');
  }

  /** "blur" event callback */
  private removeActiveClass = () => {
    const container = this.elementRef.nativeElement;
    if (!container.lastChild.value) {
      this.renderer.removeClass(container, 'active');
    }
  }
}
