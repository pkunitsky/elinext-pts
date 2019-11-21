import Pageable from './Pageable.model';
import Sort from './Sort.model';
import Group from './Group.model';

export default interface PageGroup {
  content?: Group[];
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
