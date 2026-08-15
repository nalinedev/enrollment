import dayjs from 'dayjs';

export interface IOrganizationMember {
  id?: number;
  legalName?: string;
  tradeName?: string | null;
  registrationNumber?: string | null;
  taxNumber?: string | null;
  legalForm?: string | null;
  registrationDate?: dayjs.Dayjs | null;
  email?: string | null;
  phoneNumber?: string | null;
  website?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<IOrganizationMember> = {};
