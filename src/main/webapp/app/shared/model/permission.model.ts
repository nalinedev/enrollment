import { ICooperativeRole } from 'app/shared/model/cooperative-role.model';

export interface IPermission {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  resource?: string | null;
  action?: string | null;
  active?: boolean;
  roleses?: ICooperativeRole[] | null;
}

export const defaultValue: Readonly<IPermission> = {
  active: false,
};
