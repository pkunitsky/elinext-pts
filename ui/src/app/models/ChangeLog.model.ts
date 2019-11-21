import { LogEventType } from './Types';

export default interface ChangeLog {
  createdDate?: string;
  eventType?: LogEventType;
  id?: number;
  userFirstName?: string;
  userLastName?: string;
  values?: any;
}
