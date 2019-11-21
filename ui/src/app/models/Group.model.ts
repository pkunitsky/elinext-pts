import User from './User.model';

export default interface Group {
  availableUsers?: User[];
  id?: number;
  message?: string;
  name: string; // Group name
  projectIds?: number[];
  users?: User[];
}
