import PageTimeReport from './PageTimeReport.model';
import { StatusType, DataType, TaskPriorityType } from './Types';

export default interface Task {
  archived?: boolean;
  assigneeEmail?: string;
  changedDate?: string; // It is installed and changed on the server.
  code?: string; // The code is generated on the server.
  deadline?: string; // Must be later than the start date.
  description: string;
  estimatedTime?: number;
  id?: number;
  ids?: number[];
  name: string;
  parentId?: number; // Parent task.
  percentage?: number; // minimum: 0, maximum: 100, exclusiveMinimum: false, exclusiveMaximum: false. The default value is 0.
  priority?: TaskPriorityType;
  projectId?: number;
  sizeOfReports?: number;
  startDate?: string; // Must be earlier than deadline.
  status?: StatusType;
  subtasks?: Task[];
  timeReports?: PageTimeReport;
  type?: DataType;
  lastElement?: boolean;
}
