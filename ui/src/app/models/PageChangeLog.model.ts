import Pageable from './Pageable.model';
import Sort from './Sort.model';
import ChangeLog from './ChangeLog.model';

export default interface PageChangeLog {
  content?: ChangeLog[];
  empty?: boolean;
  first?: boolean;
  last?: boolean;
  number: number;
  numberOfElements: number;
  pageable: Pageable;
  size: number;
  sort: Sort;
  totalElements: number;
  totalPages: number;
}
