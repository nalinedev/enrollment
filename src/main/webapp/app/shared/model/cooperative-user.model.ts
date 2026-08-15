import dayjs from 'dayjs';

import { IAppUser } from 'app/shared/model/app-user.model';
import { ICooperativeRole } from 'app/shared/model/cooperative-role.model';
import { ICooperative } from 'app/shared/model/cooperative.model';

export interface ICooperativeUser {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  active?: boolean;
  appUser?: IAppUser | null;
  cooperative?: ICooperative | null;
  role?: ICooperativeRole | null;
}

export const defaultValue: Readonly<ICooperativeUser> = {
  active: false,
};
