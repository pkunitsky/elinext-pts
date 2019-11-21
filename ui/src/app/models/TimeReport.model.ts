import { StatusType } from './Types';

export default interface TimeReport {
  comment: string;
  hours: number;
  id?: number;
  message?: string;
  percentage?: number; // minimum: 0, maximum: 100, exclusiveMinimum: false, exclusiveMaximum: false
  reportedDate: string;
  reporterEmail?: string;
  taskId: number;
  taskName?: string;
  taskProjectName?: string;
  taskStatus: StatusType;
}
