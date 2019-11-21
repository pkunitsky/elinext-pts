import Pageable from './Pageable.model';
import Sort from './Sort.model';
import TimeReport from './TimeReport.model';

export default interface PageTimeReport {
  content?: TimeReport[];
  empty?: boolean;
  first?: boolean;
  last?: boolean;
  number?: number;
  numberOfElements?: number;
  pageable?: Pageable;
  size?: number;
  sort?: Sort;
  totalElements?: number;
  totalPages?: number;
}
