import dayjs from 'dayjs';

export interface IIndividualMember {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  maidenName?: string | null;
  gender?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  nationality?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  occupation?: string | null;
}

export const defaultValue: Readonly<IIndividualMember> = {};
