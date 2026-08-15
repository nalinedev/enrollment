import dayjs from 'dayjs';

import { CooperativeStatus } from 'app/shared/model/enumerations/cooperative-status.model';

export interface ICooperative {
  id?: number;
  code?: string;
  name?: string;
  legalName?: string | null;
  registrationNumber?: string | null;
  taxNumber?: string | null;
  description?: string | null;
  status?: keyof typeof CooperativeStatus;
  foundedDate?: dayjs.Dayjs | null;
  email?: string | null;
  phone?: string | null;
  website?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICooperative> = {};
