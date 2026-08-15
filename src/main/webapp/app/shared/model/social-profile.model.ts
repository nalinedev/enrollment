import dayjs from 'dayjs';

import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';

export interface ISocialProfile {
  id?: number;
  maritalStatus?: keyof typeof MaritalStatus | null;
  numberOfChildren?: number | null;
  numberOfDependents?: number | null;
  educationLevel?: string | null;
  housingStatus?: string | null;
  residenceSince?: dayjs.Dayjs | null;
  disabilityStatus?: boolean | null;
  disabilityDescription?: string | null;
  socialCategory?: string | null;
  notes?: string | null;
}

export const defaultValue: Readonly<ISocialProfile> = {
  disabilityStatus: false,
};
