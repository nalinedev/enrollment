import dayjs from 'dayjs';

import { IAppUser } from 'app/shared/model/app-user.model';
import { ICooperativeBranch } from 'app/shared/model/cooperative-branch.model';
import { ICooperativeRole } from 'app/shared/model/cooperative-role.model';

export interface IBranchUser {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  active?: boolean;
  appUser?: IAppUser | null;
  branch?: ICooperativeBranch | null;
  role?: ICooperativeRole | null;
}

export const defaultValue: Readonly<IBranchUser> = {
  active: false,
};
