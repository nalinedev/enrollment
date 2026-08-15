import dayjs from 'dayjs';

import { AnimalProductionStatus } from 'app/shared/model/enumerations/animal-production-status.model';
import { AnimalSex } from 'app/shared/model/enumerations/animal-sex.model';
import { ILivestockActivity } from 'app/shared/model/livestock-activity.model';

export interface ILivestockProduction {
  id?: number;
  productionDate?: dayjs.Dayjs | null;
  animalSex?: keyof typeof AnimalSex | null;
  numberOfAnimals?: number;
  averageAgeMonths?: number | null;
  averageWeightKg?: number | null;
  productionQuantity?: number | null;
  productionUnit?: string | null;
  mortalityCount?: number | null;
  birthCount?: number | null;
  soldCount?: number | null;
  status?: keyof typeof AnimalProductionStatus;
  notes?: string | null;
  livestockActivity?: ILivestockActivity | null;
}

export const defaultValue: Readonly<ILivestockProduction> = {};
