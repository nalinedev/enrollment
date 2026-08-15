import { CooperativeRoleStatus } from 'app/shared/model/enumerations/cooperative-role-status.model';
import { IPermission } from 'app/shared/model/permission.model';

export interface ICooperativeRole {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  status?: keyof typeof CooperativeRoleStatus;
  permissionses?: IPermission[] | null;
}

export const defaultValue: Readonly<ICooperativeRole> = {};
