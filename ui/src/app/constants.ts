export const INITIAL_PAGE = 1;
export const CREATE_PROJECT = 'create project';
export const CREATE_SUBPROJECT = 'create subproject';
export const CREATE_TASK = 'create task';
export const CREATE_SUBTASK = 'create feature';
export const EDIT_TASK = 'edit task';
export const EDIT_PROJECT = 'edit project';
export const EDIT_SUBPROJECT = 'edit subproject';
export const CREATE_FEATURE = 'create feature';
export const ADD_REPORT = 'add report';
export const EDIT_REPORT = ' edit report';
export const STATUS = ['ACTIVE', 'SUBMITTED', 'IN_PLANNING', 'STARTED', 'ON_HOLD', 'COMPLETED', 'REJECTED'];
export const PROJECT_ROUTE = 'project';
export const PROJECT_WITH_TASKS_ROUTE = 'project with tasks route';
export const SUBPROJECT_ROUTE = 'subProject route';
export const ROOT_ELEMENT = 'root element';
export const CHILD_ELEMENT = 'child element';
export const INITIAL_PROJECTS_TABLE_COLUMNS_NAME = ['name', 'code', 'projectLeadEmail', 'status', 'changedDate', 'customer'];
export const PROJECTS_TABLE_COLUMNS_NAME = ['name', 'code', 'projectLeadEmail', 'status', 'changedDate', 'customer', 'deadline', 'groupName', 'managerEmail', 'startDate'];
export const INITIAL_TASKS_TABLE_COLUMNS_NAME = ['name', 'code', 'assigneeEmail', 'status', 'changedDate'];
export const TASKS_TABLE_COLUMNS_NAME = ['name', 'code', 'assigneeEmail', 'status', 'changedDate', 'deadline', 'estimatedTime', 'percentage', 'priority', 'startDate'];
export const INFO_NAMES = ['projectLeadEmail', 'assigneeEmail', 'customer', 'changedDate', 'deadline', 'percentage', 'priority', 'startDate'];
export const VISIBLE_PAGES = 5;
export const MIDDLE_OF_THE_INITIAL_PAGINATION = 3;
export const STEP_TO_EDGE = 2;
export const STATUS_PRETTIER = {
  ACTIVE: 'Active',
  SUBMITTED: 'Submitted',
  IN_PLANNING: 'In planning',
  STARTED: 'Started',
  ON_HOLD: 'On hold',
  COMPLETED: 'Completed',
  REJECTED: 'Rejected'
};
export const STATUS_COLORS = {
  ACTIVE: '#31C1FF',
  SUBMITTED: '#005AAD',
  IN_PLANNING: '#2C333F',
  STARTED: '#F2C94C',
  ON_HOLD: '#C4C4C4',
  COMPLETED: '#04D300',
  REJECTED: '#EF0D33'
};
export const SELECTION_OPTIONS = {
  name: 'Name',
  code: 'Code',
  projectLeadEmail: 'Lead',
  status: 'Status',
  changedDate: 'Changed',
  customer: 'Customer',
  deadline: 'Deadline',
  groupName: 'Group',
  managerEmail: 'Manager',
  startDate: 'Start date',
  assigneeEmail: 'Assigned to',
  estimatedTime: 'Estimated time',
  percentage: 'Percentage',
  priority: 'Priority',
};
export const MOBILE_HEADER_HEIGHT = 57;
export const TABLET_MAX_WIDTH = 767;
export const MOBILE_MAX_WIDTH = 577;

export const PRIORITY_TYPES = ['NORMAL', 'LOW', 'ASAP', 'HIGH'];
export const ROUTE_TYPE = {
  PROJECT: 'projects',
  SUBPROJECT: 'projects',
  TASK: 'tasks',
  SUBTASK: 'tasks',
  TIME_REPORT: 'reports'
};
