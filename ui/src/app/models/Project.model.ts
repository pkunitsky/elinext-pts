import JoinedProject from './JoinedProject.model';
import { StatusType, DataType } from './Types';

export default interface Project {
  archived?: boolean;
  changedDate?: any;
  code?: string; // The code is generated on the server.
  customer?: string;
  deadline?: any; // Must be later than the start date.
  description?: string;
  groupName?: string;
  id?: number;
  ids?: number[];
  joinedProjects?: JoinedProject;
  managerEmail?: string;
  message?: string;
  name: string;
  parentId?: number; // Parent project.
  projectLeadEmail?: string;
  startDate?: any; // Must be earlier than deadline.
  status?: StatusType;
  subprojects?: Project[];
  type?: DataType;
  lastElement?: boolean; // Using in NestedRowDirective
}
