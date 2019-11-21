export interface AuthUserModel {
  email: string;
  role: UserRole;
}

export enum UserRole {
  ADMIN = 'admin',
  MANAGER = 'manager',
  EMPLOYEE = 'employee'
}
