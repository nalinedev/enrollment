import { ICrop } from 'app/shared/model/crop.model';

export interface ICropVariety {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  origin?: string | null;
  maturityDays?: number | null;
  yieldPotential?: number | null;
  diseaseResistance?: string | null;
  active?: boolean;
  crop?: ICrop | null;
}

export const defaultValue: Readonly<ICropVariety> = {
  active: false,
};
