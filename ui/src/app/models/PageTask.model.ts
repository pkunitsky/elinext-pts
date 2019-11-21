import Pageable from './Pageable.model';
import Sort from './Sort.model';
import Task from './Task.model';

export default interface PageTask {
  content?: Task[];
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
