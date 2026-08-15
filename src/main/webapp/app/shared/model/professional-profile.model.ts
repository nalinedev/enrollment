import dayjs from 'dayjs';

export interface IProfessionalProfile {
  id?: number;
  employmentStatus?: string | null;
  employerName?: string | null;
  jobTitle?: string | null;
  profession?: string | null;
  sector?: string | null;
  yearsOfExperience?: number | null;
  monthlyIncome?: number | null;
  annualIncome?: number | null;
  employmentStartDate?: dayjs.Dayjs | null;
  employerLocation?: string | null;
  notes?: string | null;
}

export const defaultValue: Readonly<IProfessionalProfile> = {};
