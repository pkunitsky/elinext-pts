import Project from './Project.model';
import PageChangeLog from './PageChangeLog.model';
import User from './User.model';
import Task from './Task.model';
import TimeReport from './TimeReport.model';

export default interface DialogData {
  type: string;
  selected?: number;
  projects?: Project[];
  leads?: string[];
  groups?: string[];
  managers?: string[];
  instance?: Project | Task;
  task?: Task;
  pageChangesLog?: PageChangeLog;
  id?: number;
  route?: string;
  users?: User[];
  tasks?: string[];
  report?: TimeReport;
}
