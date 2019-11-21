import Project from './Project.model';
import Pageable from './Pageable.model';
import Sort from './Sort.model';

export default interface PageProject {
  content?: Project[];
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
