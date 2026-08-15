import dayjs from 'dayjs';

import { AppUserStatus } from 'app/shared/model/enumerations/app-user-status.model';
import { IUser } from 'app/shared/model/user.model';

export interface IAppUser {
  id?: number;
  employeeNumber?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  jobTitle?: string | null;
  department?: string | null;
  status?: keyof typeof AppUserStatus;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAppUser> = {};
