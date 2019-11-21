import { Component, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { VISIBLE_PAGES, MIDDLE_OF_THE_INITIAL_PAGINATION, STEP_TO_EDGE, INITIAL_PAGE } from '../../constants';

@Component({
  selector: 'pts-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent implements OnChanges {
  @Input() public total: number;
  public currentPage = INITIAL_PAGE;
  public paginationPages: number[];
  @Output() nextPage = new EventEmitter();

  /** Update pagination body when total is received */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes.total.currentValue) {
      this.setPaginationPages();
    }
  }

  /** Calculate beginning and end of pagination body */
  private setPaginationPages(): void {
    if (this.total === 0) {
      this.paginationPages = [];
    } else if (this.total <= VISIBLE_PAGES) {
      this.fillPaginationPages(INITIAL_PAGE, this.total);
    } else if (this.currentPage <= MIDDLE_OF_THE_INITIAL_PAGINATION && this.total > VISIBLE_PAGES) {
      this.fillPaginationPages(INITIAL_PAGE, VISIBLE_PAGES);
    } else if (this.currentPage > MIDDLE_OF_THE_INITIAL_PAGINATION && this.currentPage <= this.total - STEP_TO_EDGE) {
      this.fillPaginationPages(this.currentPage - STEP_TO_EDGE, this.currentPage + STEP_TO_EDGE);
    } else if (this.currentPage > MIDDLE_OF_THE_INITIAL_PAGINATION && this.currentPage > this.total - STEP_TO_EDGE) {
      this.fillPaginationPages(this.total - (VISIBLE_PAGES - 1), this.total);
    }
  }

  /** Create array with values for layout */
  private fillPaginationPages(from: number, to: number): void {
    const pages = [];
    for (let i = from; i <= to; i++) {
      pages.push(i);
    }
    this.paginationPages = pages;
  }

  /** Decrease current page */
  public onPreviousClick(): void {
    if (this.currentPage === INITIAL_PAGE) {
      return;
    }
    --this.currentPage;
    this.updatePage();
  }

  /** Increase current page */
  public onNextClick(): void {
    if (this.currentPage === this.total) {
      return;
    }
    ++this.currentPage;
    this.updatePage();
  }

  /** Set new current page */
  public onPageClick(page: number): void {
    this.currentPage = page;
    this.updatePage();
  }

  /** Decrease current page by value */
  public onPreviousEllipsisClick(): void {
    this.currentPage = this.currentPage - VISIBLE_PAGES;
    this.updatePage();
  }

  /** Increase current page by value */
  public onNextEllipsisClick(): void {
    this.currentPage = this.currentPage + VISIBLE_PAGES;
    this.updatePage();
  }

  /** Update values */
  private updatePage(): void {
    this.nextPage.emit(this.currentPage);
    this.setPaginationPages();
  }
}
