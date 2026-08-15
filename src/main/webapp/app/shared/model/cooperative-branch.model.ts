import dayjs from 'dayjs';

import { ICooperative } from 'app/shared/model/cooperative.model';
import { CooperativeStatus } from 'app/shared/model/enumerations/cooperative-status.model';
import { ILocation } from 'app/shared/model/location.model';

export interface ICooperativeBranch {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  phone?: string | null;
  email?: string | null;
  status?: keyof typeof CooperativeStatus;
  openingDate?: dayjs.Dayjs | null;
  closingDate?: dayjs.Dayjs | null;
  cooperative?: ICooperative | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<ICooperativeBranch> = {};
