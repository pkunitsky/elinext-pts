import { Directive, ElementRef, Renderer2, OnChanges, Input } from '@angular/core';

@Directive({
  selector: '[ptsSettingsIcon]'
})
export class SettingsIconDirective implements OnChanges {
  @Input() private isOpen: boolean;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) { }

  ngOnChanges(changes): void {
    const settingsIcon = this.elementRef.nativeElement;
    /** Toggle CSS classes for settings icon */
    changes.isOpen.currentValue
      ? this.renderer.addClass(settingsIcon, 'table-settings-icon-active')
      : this.renderer.removeClass(settingsIcon, 'table-settings-icon-active');
  }
}
