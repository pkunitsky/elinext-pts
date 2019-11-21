import Project from './Project.model';
import Task from './Task.model';
import TimeReport from './TimeReport.model';

export default interface CreateEditData {
  id?: number;
  type?: string;
  instance?: Project | Task | TimeReport; // edit
}
