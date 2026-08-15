import dayjs from 'dayjs';

import { IMember } from 'app/shared/model/member.model';

export interface IFamilyMember {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  relationship?: string;
  gender?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  nationality?: string | null;
  phoneNumber?: string | null;
  occupation?: string | null;
  dependent?: boolean | null;
  notes?: string | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<IFamilyMember> = {
  dependent: false,
};
